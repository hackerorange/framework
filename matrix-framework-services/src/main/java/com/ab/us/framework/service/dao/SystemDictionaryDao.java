package com.ab.us.framework.service.dao;

import com.ab.us.framework.db.dao.impl.BaseDaoImpl;
import com.ab.us.framework.db.entity.DataSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author Zhongchongtao
 */
public class SystemDictionaryDao extends BaseDaoImpl {
    private DataSchema schema;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public SystemDictionaryDao(DataSchema universesunCommon) {
        this.setDataSchema(universesunCommon);
    }

    public DataSchema getSchema() {
        return schema;
    }

    protected SystemDictionaryDao setSchema(DataSchema schema) {
        this.schema = schema;
        return this;
    }

    public Logger getLogger() {
        return logger;
    }

    public SystemDictionaryDao setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    @PostConstruct
    public void postConstruct() {
        this.schema = DataSchema.GALAXY_APPLIFE;
    }

    public void init() {
        this.setDataSchema(this.schema);
    }

}
