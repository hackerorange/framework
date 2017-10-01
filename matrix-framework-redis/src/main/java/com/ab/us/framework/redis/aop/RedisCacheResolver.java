package com.ab.us.framework.redis.aop;

import static com.ab.us.framework.redis.constant.RedisBaseExceptionType.REDIS_MISSING_CONFIG;

import java.util.Map;

import com.ab.us.framework.redis.annotation.RedisCachePut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.ab.us.framework.core.spring.SpringApplicationContextHolder;
import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.redis.annotation.RedisCacheable;
import com.alibaba.fastjson.JSONObject;

@Aspect
@Component
public class RedisCacheResolver {

	private Logger logger = LoggerFactory.getLogger( getClass() );

	@Around("@annotation(redisCacheable)")
	public Object doSomeThing(ProceedingJoinPoint pjp, RedisCachePut redisCacheable) throws Throwable {

		StringRedisTemplate stringRedisTemplate;
		if (StringUtil.isEmpty( redisCacheable.redisTemplate() )) {
			stringRedisTemplate = SpringApplicationContextHolder.getBean( StringRedisTemplate.class );
		} else {
			stringRedisTemplate = SpringApplicationContextHolder.getBean( redisCacheable.redisTemplate(), StringRedisTemplate.class );
		}
		// 如果没有找到定义的RedisTemplate，则直接抛出Redis配置异常
		if (TypeChecker.isNull( stringRedisTemplate )) {
			throw REDIS_MISSING_CONFIG.generateBaseException();
		}
		// 拼接Key
		String tmpKey = redisCacheable.cacheName() + ":#{" + redisCacheable.key() + "}";
		// 生成一个新都SpringEl表达式解析器
		ExpressionParser parser = new SpelExpressionParser();
		// 生成表达式
		Expression expression = parser.parseExpression( tmpKey, new TemplateParserContext() );
		// 根据pjp生成上下文
		EvaluationContext context = new StandardEvaluationContext( pjp );
		// 获取参数
		Object[] args = pjp.getArgs();
		// 获取参数名称
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		String[] parameterNames = methodSignature.getParameterNames();
		// 将参数放到SpEl上下文中
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable( parameterNames[ i ], args[ i ] );
		}
		tmpKey = expression.getValue( context, String.class );
		logger.info( "从缓存中获取对象，生成的Key为[{}]" + tmpKey );
		BoundHashOperations<String, String, Object> ops = stringRedisTemplate.boundHashOps( tmpKey );
		Map<String, Object> entries = ops.entries();
		if (TypeChecker.isEmpty( entries )) {
			Object proceed = pjp.proceed( pjp.getArgs() );
			if (TypeChecker.isNull( proceed )) {
				return null;
			}
			JSONObject jsonObject = JSONObject.parseObject( JSONObject.toJSONString( proceed ) );
			ops.putAll( jsonObject );
			return proceed;
		}
		JSONObject jsonObject = new JSONObject();
		entries.forEach( jsonObject::put );
		// noinspection unchecked
		Object o = jsonObject.toJavaObject( ((MethodSignature) pjp.getSignature()).getReturnType() );
		logger.info( JSONObject.toJSONString( o ) );
		return o;

	}



}
