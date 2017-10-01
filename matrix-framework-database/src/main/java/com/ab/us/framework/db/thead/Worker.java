package com.ab.us.framework.db.thead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Worker implements Runnable {

	protected Logger logger = LoggerFactory.getLogger(Worker.class);

	private int taskId;
	private String taskName;
	private boolean isrunning = true;

	public abstract boolean doTask();

	public Worker(int taskId, String taskName) {
		super();
		this.taskId = taskId;
		this.taskName = taskName + taskId;
		Activity.registTask(getTaskName());
	}

	@Override
	public void run() {
		logger.info("[" + this.getClass().getSimpleName() + "][Task Start]");
		try {
			if (doTask()) {
				logger.info("[" + this.getClass().getSimpleName() + "][Task Done]");
			} else {
				logger.error("[" + this.getClass().getSimpleName() + "][Task Fail]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[" + this.getClass().getSimpleName() + "][Task Error " + e.getMessage() + "]");
		} finally {
			Activity.die(getTaskName());
		}
	}

	public boolean isRunning() {
		return isrunning;
	}

	protected void setRunning(boolean isrunning) {
		this.isrunning = isrunning;
	}

	public void stop() {
		isrunning = false;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName + "-" + taskId;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
