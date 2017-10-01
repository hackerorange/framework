package com.ab.us.framework.db.db.tool;

import com.ab.us.framework.db.entity.DataSchema;

@SuppressWarnings("WeakerAccess")
public class EntityGenerator {

	private GenEntitytool tool = new GenEntitytool();

	public EntityGenerator(DataSchema dataSchema) {
		tool.setDataSchema( dataSchema );
	}

	/**
	 * 生成数据库实体
	 *
	 * @param tableName
	 *            表名
	 */
	public void generateEntity(String tableName) {
		generateEntity( tableName, "T_", "_", "package com.ab.usps.entity.db;" );
	}

	/**
	 * 生成数据库实体
	 *
	 * @param tableName
	 *            表名
	 * @param prefix
	 *            表名前缀，一般为 "T_"
	 * @param split
	 *            分隔符，一般为"_"
	 */
	public void generateEntity(String tableName, String prefix, String split, String packageName) {
		tool.setPackageName( packageName );
		tool.tableToEntity( tableName, prefix, split );
	}
}