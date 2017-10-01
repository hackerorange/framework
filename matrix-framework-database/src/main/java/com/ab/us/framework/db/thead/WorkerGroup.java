package com.ab.us.framework.db.thead;

import java.util.ArrayList;
import java.util.List;

public class WorkerGroup extends ThreadGroup {

	private static List<String> groupList = new ArrayList<String>();

	public WorkerGroup(String name) {
		super(name);
		groupList.add(name);
	}

	/**
	 * 获取线程池列表
	 *
	 * @return
	 */
	public static List<String> getGroupList(){
		return groupList;
	}

}
