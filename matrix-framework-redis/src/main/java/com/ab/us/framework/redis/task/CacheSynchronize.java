package com.ab.us.framework.redis.task;

import static com.ab.us.framework.redis.constant.CacheSynchronizeConstant.CACHE_VERSION;
import static com.ab.us.framework.redis.constant.CacheSynchronizeConstant.DB_VERSION;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;
import com.alibaba.fastjson.JSONObject;

@Component
public class CacheSynchronize {

	private final StringRedisTemplate	stringRedisTemplate;
	private Logger						logger	= LoggerFactory.getLogger( getClass() );

	public CacheSynchronize(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Async
	public void synchronizeDataBase(Object target, Method method, String cacheKey) throws Exception {
		logger.info( StringUtil.DOUBLE_SPLIT_LINE_STRING );
		logger.info( "开始同步缓存[{}]", cacheKey );
		try {
			BoundHashOperations<String, String, Object> ops = stringRedisTemplate.boundHashOps( cacheKey );
			Map<String, Object> entries = ops.entries();
			if (TypeChecker.isEmpty( entries )) {
				logger.error( "缓存已经被清，缓存Key为[{}]", cacheKey );
				return;
			}
			List<Object> objects = ops.multiGet( Arrays.asList( CACHE_VERSION, DB_VERSION ) );
			if (ObjectUtils.nullSafeEquals( objects.get( 0 ), objects.get( 1 ) )) {
				logger.info( "缓存中的版本和数据库版本相同，不需要更新数据库" );
				return;
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.putAll( entries );
			Object o = jsonObject.toJavaObject( method.getParameters()[ 1 ].getType() );
			if ((Boolean) method.invoke( target, false, o )) {
				logger.info( "同步成功" );
				// 将数据库版本修改为缓存版本
				ops.put( DB_VERSION, objects.get( 0 ) );
			} else {
				logger.info( "同步返回False,数据库暂时不是最新版本，CacheKey[{}]，清空缓存数据", cacheKey );
				stringRedisTemplate.delete( cacheKey );
			}
		} finally {
			logger.info( "同步结束" );
			logger.info( StringUtil.DOUBLE_SPLIT_LINE_STRING );
		}

	}
}