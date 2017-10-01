package com.ab.us.framework.redis.support;

        import com.ab.us.framework.core.utils.ClassUtils;
        import com.ab.us.framework.redis.annotation.CacheExpireTime;
        import com.ab.us.framework.redis.cache.CustomizedRedisCacheManager;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.boot.context.event.ApplicationReadyEvent;
        import org.springframework.context.ApplicationListener;
        import org.springframework.stereotype.Component;

        import java.lang.reflect.Method;
        import java.util.List;

/**
 * @author Zhongchongtao
 */
@Component
public class RedisCacheSupport implements ApplicationListener<ApplicationReadyEvent> {

    private static final String US_BASE_PACKAGE = "com.ab.us";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Class<?>> allClassUnderPackage = ClassUtils.findAllClassUnderPackage(US_BASE_PACKAGE);
        allClassUnderPackage.forEach(this::findCacheExpireInfoFromClass);
    }

    /**
     * 从类中，获取缓存过期时间信息
     *
     * @param clazz 类
     */
    private void findCacheExpireInfoFromClass(Class clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            findCacheExpireInfoFromMethod(method);
        }
    }

    /**
     * 从方法中获取缓存过期时间信息
     *
     * @param method 方法
     */
    private void findCacheExpireInfoFromMethod(Method method) {
        if (method.isAnnotationPresent(CacheExpireTime.class)) {
            CacheExpireTime annotation = method.getAnnotation(CacheExpireTime.class);
            for (String cacheNames : annotation.cacheNames()) {
                CustomizedRedisCacheManager.addExpireTime(cacheNames, annotation);
                logger.info("正在初始化缓存过期时间计算，缓存名称[{}],缓存过期类型[{}],默认过期时间[{}]", cacheNames, annotation.expireType().name(), annotation.defaultExpireTime());
            }
        }
    }
}
