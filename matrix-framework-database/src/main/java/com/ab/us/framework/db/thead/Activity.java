package com.ab.us.framework.db.thead;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程活动类
 *
 * @author xusisheng
 *
 */
public class Activity {

	private static Map<String, Boolean> isActivity = new HashMap<String, Boolean>();

	public static void registTask(String taskName) {
		isActivity.put(taskName, true);
	}

	public static void die(String taskName) {
		isActivity.put(taskName, false);
	}

	public static boolean isAlive(String taskName) {
		if (isActivity.containsKey(taskName)) {
			return isActivity.get(taskName);
		}
		return false;
	}

	/**
	 * 任务活动的线程数
	 *
	 * @param task 任务名称
	 * @return
	 */
	public static int aliveCount(String task) {
		int alive = 0;
		for (String taskName : isActivity.keySet()) {
			if (taskName.startsWith(task)) {
				alive += isActivity.get(taskName) ? 1 : 0;
			}
		}
		return alive;
	}
}
