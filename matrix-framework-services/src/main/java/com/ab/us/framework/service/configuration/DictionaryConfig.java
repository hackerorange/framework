package com.ab.us.framework.service.configuration;

import com.ab.us.framework.db.entity.DataSchema;
import com.ab.us.framework.service.dao.SystemDictionaryDao;
import com.ab.us.framework.service.dictionray.SystemDictionaryCacheService;
import com.ab.us.framework.service.dictionray.SystemDictionaryCacheServiceImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zhongchongtao
 */
@Configuration
@EnableAutoConfiguration
public class DictionaryConfig {

    @Bean
    public SystemDictionaryCacheService systemDictionaryCacheService(SystemDictionaryDao systemDictionaryDao) {
        return new SystemDictionaryCacheServiceImpl(systemDictionaryDao);
    }

    @Bean
    public SystemDictionaryDao systemDictionaryDao() {
        return new SystemDictionaryDao(DataSchema.UNIVERSESUN_COMMON);
    }

}
