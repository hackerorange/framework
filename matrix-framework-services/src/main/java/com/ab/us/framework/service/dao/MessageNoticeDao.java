package com.ab.us.framework.service.dao;

import com.ab.us.framework.db.dao.impl.BaseDaoImpl;
import com.ab.us.framework.db.entity.DataSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by ZhongChongtao on 2017/2/22.
 */
public class MessageNoticeDao extends BaseDaoImpl {
    private DataSchema schema;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DataSchema getSchema() {
        return schema;
    }

    protected MessageNoticeDao setSchema(DataSchema schema) {
        this.schema = schema;
        return this;
    }

    public Logger getLogger() {
        return logger;
    }

    public MessageNoticeDao setLogger(Logger logger) {
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
