package com.ab.us.framework.redis.aop;

import static com.ab.us.framework.redis.constant.CacheSynchronizeConstant.CACHE_VERSION;
import static com.ab.us.framework.redis.constant.CacheSynchronizeConstant.DB_VERSION;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.core.utils.UUIDUtils;
import com.ab.us.framework.redis.annotation.RedisCacheConfig;
import com.ab.us.framework.redis.model.CacheModel;
import com.ab.us.framework.redis.support.CacheDriver;
import com.ab.us.framework.redis.task.CacheSynchronize;
import com.alibaba.fastjson.JSONObject;

@Aspect
@Component
@EnableAsync
public class CacheDriverResolver {

	private final CacheSynchronize		cacheSynchronize;
	private final StringRedisTemplate	stringRedisTemplate;
	private Logger						logger	= LoggerFactory.getLogger( getClass() );

	@Autowired
	public CacheDriverResolver(CacheSynchronize cacheSynchronize, StringRedisTemplate stringRedisTemplate) {
		this.cacheSynchronize = cacheSynchronize;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Around("execution(boolean com.ab.us.framework.redis.support.CacheDriver.merge(..))")
	public boolean resolveCacheMerge(ProceedingJoinPoint pjp) throws Throwable {
		// 获取实现类
		CacheDriver target = (CacheDriver) pjp.getTarget();
		boolean isSynchronize = (Boolean) pjp.getArgs()[ 0 ];
		// 如果没有配置Redis
		if (!target.getClass().isAnnotationPresent( RedisCacheConfig.class )) {
			return (boolean) pjp.proceed( pjp.getArgs() );
		}
        RedisCacheConfig annotation = target.getClass().getAnnotation( RedisCacheConfig.class );
		// 如果不需要异步处理
		if (!isSynchronize) {
			// 先执行数据库操作
			Boolean proceed = (Boolean) pjp.proceed( pjp.getArgs() );
			// 如果没有处理成功，更新缓存
			if (!proceed) {
				return false;
			}
		}
		// 获取要缓存的model
		CacheModel paramModel = (CacheModel) pjp.getArgs()[ 1 ];
		// 如果参数不是空，则执行
		if (TypeChecker.isNull( paramModel )) {
			logger.info( "合并数据的时候，数据不可以为空！" );
			return false;
		}
		// 如果参数bean无法获取Key,合并失败
		Object key = paramModel.getKey();
		if (TypeChecker.isNull( key )) {
			logger.info( "合并数据的时候，主键不可以为空,合并失败！" );
			// 返回错误信息
			return false;
		}

		if (TypeChecker.isNull( paramModel.getKey() )) {
			logger.info( "获取缓存实体的时候，Key不可为空！" );
			return false;
		}
		// noinspection StringBufferMayBeStringBuilder
		StringBuffer cacheLocation = new StringBuffer( annotation.location() );
		// 获取返回类型
		Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();
		// 如果没有设置默认地址，则根据返回类型的类名，生成缓存路径，前缀为matrix
		if (StringUtil.isEmpty( annotation.location() )) {
			cacheLocation
					.append( "matrix:" )
					.append( returnType.getSimpleName() );
		}
		cacheLocation
				.append( ":" )
				.append( paramModel.getKey() );
		// 获取要缓存的model
		String cacheKey = cacheLocation.toString();

		BoundHashOperations<String, String, Object> ops = stringRedisTemplate.boundHashOps( cacheKey );

		Map<String, Object> entries = ops.entries();
		// 如果缓存中没有对象
		if (TypeChecker.isEmpty( entries )) {
			// noinspection unchecked
			CacheModel targetCacheModel = target.getCacheModel( key );
			if (TypeChecker.isNull( targetCacheModel )) {
				logger.info( "根据主键ID,没有找到对象，类名[{}] Key[{}]", target.getClass().getName(), key );
				return false;
			}
			// 数据库中的记录
			JSONObject jsonObject = new JSONObject( (JSONObject) JSONObject.toJSON( targetCacheModel ) );
			// 要更新的字段
			((JSONObject) JSONObject.toJSON( paramModel )).forEach( (s, o) -> {
				if (!TypeChecker.isEmptyObject( o )) {
					jsonObject.put( s, o );
				}
			} );
			// JsonObject保存到缓存中
			ops.putAll( jsonObject );
		} else {
			// 部分更新缓存
			JSONObject jsonObject = new JSONObject();
			// 缓存非空字段
			((JSONObject) JSONObject.toJSON( paramModel )).forEach( (s, o) -> {
				if (!TypeChecker.isEmptyObject( o )) {
					jsonObject.put( s, o );
				}
			} );
			// 保存到缓存中
			ops.putAll( jsonObject );
		}
		// 如果是异步处理，发送到异步处理线程
		if (isSynchronize) {
			MethodSignature signature = (MethodSignature) pjp.getSignature();
			// 生成缓存版本号
			String generate = UUIDUtils.generate( true );
			ops.put( CACHE_VERSION, generate );
			cacheSynchronize.synchronizeDataBase( pjp.getTarget(), signature.getMethod(), cacheKey );
		}
		// 返回正常响应结果
		return true;
	}

	/**
	 * 获取模型AOP
	 * 
	 * @param pjp
	 *            模型
	 * @return 获取的对象信息
	 * @throws Throwable
	 *             发生异常，抛出异常
	 */
	@Around("execution(* com.ab.us.framework.redis.support.CacheDriver.getCacheModel(..))")
	public Object resolveGetCacheModel(ProceedingJoinPoint pjp) throws Throwable {
		// 获取实现类
		CacheDriver target = (CacheDriver) pjp.getTarget();

		// 如果没有配置Redis
		if (!target.getClass().isAnnotationPresent( RedisCacheConfig.class )) {
			return pjp.proceed( pjp.getArgs() );
		}
		RedisCacheConfig annotation = target.getClass().getAnnotation( RedisCacheConfig.class );
		Object paramModel = pjp.getArgs()[ 0 ];
		if (TypeChecker.isNull( paramModel )) {
			logger.info( "获取缓存实体的时候，Key不可为空！" );
			return null;
		}
		// noinspection StringBufferMayBeStringBuilder
		StringBuffer cacheLocation = new StringBuffer( annotation.location() );
		// 获取返回类型
		Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();
		// 如果没有设置默认地址，则根据返回类型的类名，生成缓存路径，前缀为matrix
		if (StringUtil.isEmpty( annotation.location() )) {
			cacheLocation
					.append( "matrix:" )
					.append( returnType.getSimpleName() );
		}

		cacheLocation
				.append( ":" )
				.append( paramModel );
		// 获取要缓存的model
		String cacheKey = cacheLocation.toString();
		// 如果参数不是空，则执行
		BoundHashOperations<String, String, Object> ops = stringRedisTemplate.boundHashOps( cacheKey );
		// 从缓存中取得数据
		Map<String, Object> entries = ops.entries();
		// 如果缓存中没有对象
		if (!TypeChecker.isEmpty( entries )) {
			// 缓存中查找到对象，直接拼接对象
			JSONObject jsonObject = new JSONObject( entries );
			// 返回正常响应结果
			try {
				// noinspection unchecked
				CacheModel o = (CacheModel) jsonObject.toJavaObject( returnType );
				// 如果拼接的对象不为空，直接返回此对象
				if (!TypeChecker.isNull( o ) && TypeChecker.notNull( o.getKey() )) {
					return o;
				}
			} catch (Exception e) {
				logger.error( e.getMessage(), e );
			}
		}
		// 缓存中没有数据的时候
		// noinspection unchecked
		CacheModel targetCacheModel = (CacheModel) pjp.proceed(pjp.getArgs());
		if (TypeChecker.isNull( targetCacheModel )) {
			logger.info( "根据主键ID,从缓存中没有没有找到对象，类名[{}] Key[{}]", target.getClass().getName(), cacheKey );
			return null;
		}
		// 数据库中的记录
		JSONObject jsonObject = new JSONObject( (JSONObject) JSONObject.toJSON( targetCacheModel ) );
		String version = UUIDUtils.generate( true );
		// 生成版本号
		jsonObject.put( DB_VERSION, version );
		// 生成缓存版本号
		jsonObject.put( CACHE_VERSION, version );
		// JsonObject保存到缓存中
		ops.putAll( jsonObject );
		// 将目标缓存模型直接返回回去；
		return targetCacheModel;
	}
}
