package com.ab.us.framework.db.dao.impl;

import static com.ab.us.framework.db.util.SqlTools.getDeleteSql;
import static com.ab.us.framework.db.util.SqlTools.getInsertSql;
import static com.ab.us.framework.db.util.SqlTools.getSelectSql;
import static com.ab.us.framework.db.util.SqlTools.getUpdateSql;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.dao.BaseDao;
import com.ab.us.framework.db.db.DBPool;
import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.db.entity.DataSchema;
import com.ab.us.framework.db.util.PageInfo;
import com.ab.us.framework.db.util.SqlTools;

/**
 * @ClassName: BaseDaoImpl
 * @Description: TODO(dao实现类)
 * @author xusisheng
 * @date 2017-01-23 14:25:19
 *
 */
public class BaseDaoImpl extends BaseDaoTemplate implements BaseDao {

	public static final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	public void setDataSchema(DataSchema dataSchema) {
		super.setDataSource(DBPool.getInstance().getDataSource(dataSchema));
	}

	@Override
	public long insert(BaseEntity entity) {
		return super.insert(getInsertSql(entity), null);
	}

	@Override
	public Integer insertForKeys(BaseEntity entity) {
		return super.insertForKeys(getInsertSql(entity), null);
	}

	@Override
	public long updateByPk(BaseEntity entity) {
		return super.update(getUpdateSql(entity, true));
	}

	@Override
	public long update(BaseEntity entity, boolean isPk) {
		return super.update(getUpdateSql(entity, isPk));
	}

	@Override
	public long update(String sql) {
		return super.update(sql);
	}

	@Override
	public long update(String sql, Object... args) {
		return super.update(sql, args);
	}

	@Override
	public int[] batchUpdate(String sql, Object[][] objs) {
		return super.batchUpdate(sql, objs);
	}

	@Override
	public long delete(BaseEntity entity) {
		return super.update(getDeleteSql(entity));
	}

	@Override
	public long delete(BaseEntity entity, boolean reqPk) {
		return super.update(getDeleteSql(entity, reqPk));
	}

	@Override
	public List<? extends BaseEntity> queryByEntity(BaseEntity entity) {
		return super.find(entity.getClass(), getSelectSql(entity));
	}

	@Override
	public BaseEntity queryEntity(BaseEntity entity) {
		return super.findFirst(entity.getClass(), SqlTools.getSelectSql(entity));
	}

	@Override
	public Map<String, Object> queryMapEntity(BaseEntity entity) {
		return super.findFirst(SqlTools.getSelectSql(entity));
	}

	@Override
	public long queryCount(BaseEntity entity) {
		return super.count(SqlTools.getSelectCountSql(entity), null);
	}

	@Override
	public int insertForKeys(String sql, Object... args) {
		return super.insertForKeys(sql, args);
	}

	@Override
	public long count(String sql, Object... params) {
		return super.count(sql, params);
	}

	@Override
	public List<Map<String, Object>> findPage(String sql, int page, int count, Object... params) {
		return super.findPage(sql, page, count, params);
	}

	@Override
	public <T> PageInfo<T> paginate(Class<T> clazz, BaseEntity entity, int page, int count) {
		return super.paginateAll(clazz, SqlTools.getSelectItemSql(entity), SqlTools.getFromWhereSql(entity), page, count, null);

	}

	@Override
	public <T> PageInfo<T> paginate(Class<T> clazz, String sql, int page, int count, Object... params) {
		return super.paginate(clazz, sql, page, count, params);

	}

	@Override
	public <T> T queryEntity(Class<T> clazz, String sql, Object[] params) {
		return super.findFirst(clazz, sql, params);
	}

	@Override
	public Map<String, Object> query(String sql, Object... params) {
		return super.findFirst(sql, params);
	}

	@Override
	public List<Map<String, Object>> queryList(String sql, Object... params) {
		return super.find(sql, params);
	}
}
