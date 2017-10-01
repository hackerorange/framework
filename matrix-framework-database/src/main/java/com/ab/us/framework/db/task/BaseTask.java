package com.ab.us.framework.db.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.monitor.MornitorEntity;

/**
 * @ClassName: BaseTask
 * @Description: TODO(抽像定时任务类)
 * @author xusisheng
 * @date 2017年03月20日16:22:49
 * @note
 * cron表达式：* * * * * *（共6位，使用空格隔开，具体如下）
 * cron表达式：*(秒0-59) *(分钟0-59) *(小时0-23) *(日期1-31) *(月份1-12或是JAN-DEC) *(星期1-7或是SUN-SAT)
 *
 */
public abstract class BaseTask extends TimerTask {

	protected static final Logger logger = LoggerFactory.getLogger("BaseTask");

	/** 前端查看任务状态 */
	public static Map<String, long[]> taskStatusMap = new HashMap<String, long[]>();

	/** 区分不同定时任务的标识 */
	protected String identify;
	/** 定时任务task的监控实体 */
	private MornitorEntity mornitor = null;
	/** 获取日期和时间 */
	protected static FastDateFormat fdf = FastDateFormat.getInstance("yyyyMMdd|HH:mm:ss");
	protected String Date;
	protected String Time;
	protected String taskRslt;// 任务执行结果

	public BaseTask(String identify) {
		this.identify = identify;
	}

	/** 定时任务完成任务的主方法 */
	public abstract void PerformTask();

	@Override
	public void run() {
		StopWatch watch = new StopWatch();
		watch.start();
		long startTime = watch.getStartTime();
		String date = fdf.format(new Date(startTime));
		String[] dateArr = date.split("\\|");
		Date = dateArr[0];
		Time = dateArr[1];
		mornitor = new MornitorEntity(identify, Date, Time);
		taskStatusMap.put(identify, new long[] { 1l, startTime });
		try {
			PerformTask();
			mornitor.setDemo(taskRslt);
		} catch (Exception se) {
			mornitor.setResult(1);
			mornitor.setDemo(se.getMessage());
		}
		watch.split();
		taskStatusMap.put(identify, new long[] { 0l, System.currentTimeMillis() });
		long usedTime = watch.getSplitTime();
		mornitor.setUsedTime(usedTime);
		mornitor.MakeDB();
	}

}
