package com.ab.us.framework.redis.support.impl;

import com.ab.us.framework.db.annotation.FieldAnnotation;
import com.ab.us.framework.db.annotation.TableAnnotation;
import com.ab.us.framework.db.db.DBPool;
import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.core.exception.BaseException;
import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.redis.support.KeySupport;
import com.ab.us.framework.db.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static com.ab.us.framework.redis.constant.RedisBaseExceptionType.TABLE_NOT_FOUND;
import static com.ab.us.framework.redis.constant.RedisBaseExceptionType.TABLE_PK_FOUND;

/**
 * @author Zhongchongtao
 */
@Component
public class KeySupportImpl implements KeySupport {

    private final StringRedisTemplate stringRedisTemplate;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public KeySupportImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Long nextPk(Class<? extends BaseEntity> tClass) {
        TableAnnotation annotation = tClass.getAnnotation(TableAnnotation.class);
        String tbName = annotation.tableName();
        if (StringUtils.isBlank(tbName)) {
            logger.warn("必须是指定表，[{}]没有指定对应的表", tClass.getTypeName());
            throw TABLE_NOT_FOUND.generateBaseException();
        }

        int count = 0;

        List<Field> annoFieldList = ClassUtils.getAnnoFieldList(tClass, FieldAnnotation.class);
        FieldAnnotation tmpPrimaryKeyField=null;
        if (annoFieldList != null && annoFieldList.size() > 0) {
            for (Field field : annoFieldList) {
                FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
                if (anno.pk()) {
                    count++;
                    tmpPrimaryKeyField = anno;
                }
            }
        }
        if (TypeChecker.isNull(tmpPrimaryKeyField)||count != 1) {
            logger.warn("主键必须只有一个，[{}]存在[{}]个主键", tbName, count);
            throw new BaseException(TABLE_PK_FOUND.getCode(), TABLE_PK_FOUND.getMessage());
        }
        FieldAnnotation primaryKeyField=tmpPrimaryKeyField;
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Long nextValue = connection.incr(("TABLE:PK:" + annotation.tableName()).getBytes());
                if (nextValue > 1 & nextValue > primaryKeyField.start()) {
                    return nextValue;
                }
                while (!connection.setNX(("lock:TABLE:PK:" + annotation.tableName()).getBytes(), "1".getBytes())) {
                    try {
                        while (connection.exists(("lock:TABLE:PK:" + annotation.tableName()).getBytes())) {
                            Thread.sleep(500);
                        }
                        nextValue = connection.incr(("TABLE:PK:" + annotation.tableName()).getBytes());
                        if (nextValue > 1 & nextValue > primaryKeyField.start()) {
                            return nextValue;
                        }
                        return this.doInRedis(connection);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try (Connection conn = DBPool.getInstance().getConnection(annotation.schema())) {
                    String sql = "SELECT MAX(" + primaryKeyField.fieldName() + ") FROM " + annotation.tableName();
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    Long aLong = resultSet.next() ? resultSet.getLong(1) : primaryKeyField.start();

                    long nextDatabaseValue = aLong + 1;
                    if (nextDatabaseValue < primaryKeyField.start()) {
                        nextDatabaseValue = primaryKeyField.start() + 1;
                    }
                    connection.incrBy(("TABLE:PK:" + annotation.tableName()).getBytes(), aLong);
                    return nextDatabaseValue;
                } catch (SQLException e) {
                    logger.error("生成主键时，数据库错误", e);
                    throw TABLE_PK_FOUND.generateBaseException();
                } finally {
                    connection.del(("lock:TABLE:PK:" + annotation.tableName()).getBytes());
                }
            }
        });
    }
}
