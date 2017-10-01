package com.ab.us.framework.redis.aop;

import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.redis.annotation.RedisDistributeLock;
import com.ab.us.framework.redis.constant.RedisBaseExceptionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Zhongchongtao
 */
@Aspect
@Component
public class RedisDistributeLuckSupport {

    private final RedisTemplate<String, String> redisTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RedisDistributeLuckSupport(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(redisDistributeLock)")
    public Object doAroundRedisKey(ProceedingJoinPoint pjp, RedisDistributeLock redisDistributeLock) throws Throwable {

        String tmpKey = "lock:" + redisDistributeLock.lockName() + ":" + redisDistributeLock.lockKey() + " .lock";
        Object[] args = pjp.getArgs();

        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(pjp);
        context.setVariable("args", args);

        Expression expression = parser.parseExpression(tmpKey, new TemplateParserContext());
        tmpKey = expression.getValue(context, String.class);
        System.out.println(tmpKey);

        Long startTime = System.currentTimeMillis();
        int count = 0;
        String finalTmpKey = tmpKey;
        //如果获得不了锁，在循环中执行睡眠操作
        while (!redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.setNX(finalTmpKey.getBytes(), "1".getBytes()))) {
            try {
                Thread.sleep(redisDistributeLock.sleepTime());
                logger.warn("没有获得Redis锁，当前尝试数量[{}]", count++);
            } catch (InterruptedException e) {
                logger.error("等待缓存锁的时候发生异常", e);
            }
            if (System.currentTimeMillis() - startTime > redisDistributeLock.timeout()) {
                throw RedisBaseExceptionType.REDIS_DISTRIBUTE_LOCK_TIME_OUT.generateBaseException();
            }
        }
        logger.warn("取得Redis锁，当前尝试数量[{}]", count);
        BoundValueOperations<String, String> redisLock = redisTemplate.boundValueOps(tmpKey);
        redisLock.expire(redisDistributeLock.expireTime(), TimeUnit.MILLISECONDS);
        try {
            startTime = System.currentTimeMillis();
            return pjp.proceed(pjp.getArgs());
        } finally {
            Object object = redisLock.get();
            Long remainTime = System.currentTimeMillis() - startTime;
            if (TypeChecker.isNull(object)) {
                logger.warn(" Redis已经提前解锁，过期时间设置的太短了，请设置一个更长的失效时间，Redis锁[{}],本次Redis锁锁定时间为[{}]", tmpKey, remainTime);
            } else {
                Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                    Long del = connection.del(finalTmpKey.getBytes());
                    return del > 0;
                });
                if (execute) {
                    logger.info("Redis锁清理成功,lock为[{}],本次Redis锁锁定时间为[{}]", redisLock, remainTime);
                } else {
                    logger.info("Redis锁清理失败,lock为[{}],本次Redis锁锁定时间为[{}]", redisLock, remainTime);
                }
            }
        }
    }
}
