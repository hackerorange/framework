package com.ab.us.framework.service.configuration;

import com.ab.us.framework.service.configuration.properties.UsPushProperties;
import com.ab.us.framework.service.dao.MessageNoticeDao;
import com.ab.us.framework.service.message.MessageNoticeService;
import com.ab.us.framework.service.message.impl.MessageNoticeServiceImpl;
import com.ab.us.framework.service.push.PushService;
import com.ab.us.framework.service.push.PushServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Zhongchongtao
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(UsPushProperties.class)
public class MessageConfig {
    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @ConditionalOnBean(name = "businessRedisTemplate")
    public MessageNoticeService messageNoticeService(MessageNoticeDao messageNoticeDao, @Qualifier("businessRedisTemplate") RedisTemplate<String, String> businessRedisTemplate) {
        return new MessageNoticeServiceImpl(messageNoticeDao, businessRedisTemplate);
    }

    @Bean
    public MessageNoticeDao messageNoticeDao() {
        return new MessageNoticeDao();
    }

    @Bean
    public PushService pushService(UsPushProperties pushProperties){
        return new PushServiceImpl(pushProperties);
    }


}
