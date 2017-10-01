package com.ab.us.framework.redis.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.redis.cache.CustomizedRedisCacheManager;
import com.ab.us.framework.redis.executor.ExpireTimeCalculator;
import com.ab.us.framework.redis.executor.impl.NextDayExpireTimeCalculator;
import com.ab.us.framework.redis.executor.impl.StaticExpireTimeCalculator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Zhongchongtao
 */
@Configuration
@EnableAutoConfiguration
public class RedisCacheManagerConfiguration implements BeanDefinitionRegistryPostProcessor {

	private static Properties properties = new Properties();

	static {
		try (InputStream resourceAsStream = RedisCacheManagerConfiguration.class.getClassLoader().getResourceAsStream( "redis.properties" )) {
			properties.load( resourceAsStream );
		} catch (IOException e) {
			System.out.println( e.getMessage() );
			properties = null;
		}
	}

	private Logger logger = LoggerFactory.getLogger( this.getClass() );

	private RedisConnectionFactory getConnectionFactory(Properties properties, String configName) {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		if (properties.containsKey( configName + ".host" )) {
			jedisConnectionFactory.setHostName( properties.getProperty( configName + ".host" ) );
		}
		if (properties.containsKey( configName + ".port" )) {
			jedisConnectionFactory.setPort( Integer.parseInt( properties.getProperty( configName + ".port", "6379" ) ) );
		}
		if (properties.containsKey( configName + ".password" )) {
			jedisConnectionFactory.setPassword( properties.getProperty( configName + ".password" ) );
		}
		if (properties.containsKey( configName + ".database" )) {
			jedisConnectionFactory.setDatabase( Integer.parseInt( properties.getProperty( configName + ".database", "0" ) ) );
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal( Integer.parseInt( properties.getProperty( "pool.maxTotal", "1000" ) ) );
		jedisPoolConfig.setMaxIdle( Integer.parseInt( properties.getProperty( "pool.maxIdle", "1000" ) ) );
		jedisPoolConfig.setMinIdle( Integer.parseInt( properties.getProperty( "pool.minIdle", "5" ) ) );
		jedisConnectionFactory.setPoolConfig( jedisPoolConfig );
		jedisConnectionFactory.afterPropertiesSet();
		return jedisConnectionFactory;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// 如果没有获取到Redis的缓存配置，直接返回
		if (TypeChecker.isNull( properties )) {
			logger.warn( "没有找到Redis配置文件" );
			return;
		}
		if (!properties.containsKey( "root" )) {
			logger.warn( "Redis配置文件中没有root属性" );
			return;
		}
		String[] roots = properties.getProperty( "root" ).split( "," );
		for (int i = 0; i < roots.length; i++) {
			String redisTemplates = roots[ i ];
			RootBeanDefinition beanDefinition = new RootBeanDefinition();// (RootBeanDefinition)
																			// BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
			beanDefinition.setBeanClass( StringRedisTemplate.class );
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();

			Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>( Object.class );
			ObjectMapper om = new ObjectMapper();
			om.setVisibility( PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY );
			om.enableDefaultTyping( ObjectMapper.DefaultTyping.NON_FINAL );
			jackson2JsonRedisSerializer.setObjectMapper( om );

			mutablePropertyValues.add( "connectionFactory", getConnectionFactory( properties, redisTemplates ) );
			mutablePropertyValues.add( "valueSerializer", jackson2JsonRedisSerializer );
			mutablePropertyValues.add( "hashValueSerializer", jackson2JsonRedisSerializer );
			beanDefinition.setPropertyValues( mutablePropertyValues );
			if (i == 0) {
				beanDefinition.setPrimary( true );
			}
			registry.registerBeanDefinition( redisTemplates + "RedisTemplate", beanDefinition );

			// CacheManager 注入
			RootBeanDefinition redisCacheManagerBeanDefinition = new RootBeanDefinition();
			ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
			constructorArgumentValues.addGenericArgumentValue( beanDefinition );
			redisCacheManagerBeanDefinition.setConstructorArgumentValues( constructorArgumentValues );
			redisCacheManagerBeanDefinition.setDependsOn( redisTemplates + "RedisTemplate" );
			redisCacheManagerBeanDefinition.setBeanClass( CustomizedRedisCacheManager.class );
			redisCacheManagerBeanDefinition.setDependsOn( redisTemplates + "RedisTemplate" );
			if (redisTemplates.equals( "dynamic" )) {
				redisCacheManagerBeanDefinition.setPrimary( true );
			}
			registry.registerBeanDefinition( redisTemplates + "CacheManager", redisCacheManagerBeanDefinition );
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// 如果没有获取到Redis的缓存配置，直接返回
		if (TypeChecker.isNull( properties )) {
			logger.warn( "没有找到Redis配置文件" );
			return;
		}
		if (!properties.containsKey( "root" )) {
			logger.warn( "Redis配置文件中没有root属性" );
			return;
		}
		for (String redisTemplates : properties.getProperty( "root" ).split( "," )) {
			StringRedisTemplate stringRedisTemplate = beanFactory.getBean( redisTemplates + "RedisTemplate", StringRedisTemplate.class );
			stringRedisTemplate.setConnectionFactory( getConnectionFactory( properties, redisTemplates ) );
			stringRedisTemplate.afterPropertiesSet();
			// 设置 CacheManager
			RedisCacheManager redisCacheManager = beanFactory.getBean( redisTemplates + "CacheManager", RedisCacheManager.class );
			redisCacheManager.setUsePrefix( true );
		}
	}

	@Bean
	public ExpireTimeCalculator nextDayExpireTimeCalculator() {
		return new NextDayExpireTimeCalculator();
	}

	@Bean
	public ExpireTimeCalculator staticExpireTimeCalculator() {
		return new StaticExpireTimeCalculator();
	}

}
