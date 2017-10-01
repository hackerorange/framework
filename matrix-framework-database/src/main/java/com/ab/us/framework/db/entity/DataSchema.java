package com.ab.us.framework.db.entity;

/**
 * @ClassName: Database
 * @Description: TODO(数据库标识，需要依据数据源不同来扩展)
 * @author xusisheng
 * @date 2017-02-10 15:55:58
 * @note 必须保证数据源名称与配置文件名（不包含后缀名）一直
 *
 */
public enum DataSchema {
    UNIVERSESUN_COMMON("universesun_common"),
    UNIVERSESUN_SPECIAL("universesun_special"),
    GALAXY_ACTIVITY("galaxy_activity"),
    GALAXY_APPLIFE("galaxy_applife"),
    GALAXY_SERVICES("galaxy_services"),
    GALAXY_SSO("galaxy_sso"),
    SSO_ACCOUNT("sso_account");

    private String schemaName;

    DataSchema(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String toSchema() {
    	return "DataSchema." + this.name();
    }
}
