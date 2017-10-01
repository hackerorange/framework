package com.ab.us.test;

import java.util.ArrayList;
import java.util.List;

import com.ab.us.framework.db.db.tool.GenEntitytool;
import com.ab.us.framework.db.entity.DataSchema;

public class GenEntityTask {

	private static List<String> list = new ArrayList<String>();

	public static void main(String[] args) {

		//添加需要生产的表列表
//		list .add("T_JOB_BLOB_TRIGGERS");
//		list .add("T_JOB_CALENDARS");
//		list .add("T_JOB_CRON_TRIGGERS");
//		list .add("T_JOB_FIRED_TRIGGERS");
//		list .add("T_JOB_JOB_DETAILS");
//		list .add("T_JOB_LOCKS");
//		list .add("T_JOB_PAUSED_TRIGGER_GRPS");
//		list .add("T_JOB_SCHEDULER_STATE");
//		list .add("T_JOB_SIMPLE_TRIGGERS");
//		list .add("T_JOB_SIMPROP_TRIGGERS");
//		list .add("T_JOB_TRIGGER_GROUP");
//		list .add("T_JOB_TRIGGER_INFO");
//		list .add("T_JOB_TRIGGER_LOG");
//		list .add("T_JOB_TRIGGER_LOG_GLUE");
//		list .add("T_JOB_TRIGGER_REGISTRY");
//		list .add("T_JOB_TRIGGERS");

		list .add("TB_MESSAGE");
		list .add("TB_MESSAGE_TEMPLATE");
		list .add("TB_MESSAGE_TYPE");

		//  如果未指定表，则获得指定登陆用户的所有表
		if (list .size() == 0) {
			GenEntitytool tool = new GenEntitytool();
			tool.setDataSchema(DataSchema.UNIVERSESUN_SPECIAL);
			list  = tool.getTableNames("UNIVERSESUN_COMMON");
		}

		for (int i = 0; i < list .size(); i++) {
			// 设置数据库表前缀及分隔符
			// 设置数据库标识
			GenEntitytool tool = new GenEntitytool();
			tool.setPackageName("package com.ab.us.message.entity");
			tool.setDataSchema(DataSchema.UNIVERSESUN_SPECIAL);
			tool.tableToEntity(list .get(i), "TB_", "_");
		}
		System.out.println("==========生产数据库表对象实体类完成============");
	}

}
