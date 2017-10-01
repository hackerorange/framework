package com.ab.us.framework.db.monitor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.ab.us.framework.core.utils.NetUtil;

/**
 * @ClassName: MornitorEntity
 * @Description: TODO(监控任务类)
 * @author xusisheng
 * @date 2017年03月20日16:23:07
 *
 */
public class MornitorEntity {

	/** 调度任务的标识 */
	public String identify;
	private static String localIp = "";
	public boolean markMornitorSingle = false;
	private ProgressEntity pro;
	private static FastDateFormat sim = FastDateFormat.getInstance("yyyyMMdd;HH:mm");

	public MornitorEntity(String identify) {
		this(identify, false);
	}

	public MornitorEntity(String identify, String startDate, String startTime) {
		this(identify, false);
		this.identify = identify;
		this.date = startDate;
		this.startTime = startTime;

	}

	/**
	 * 获取本机IP
	 *
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getIp() {
		if (StringUtils.isBlank(localIp)) {
			localIp = NetUtil.getLocIp();
		}
		return localIp;
	}

	public MornitorEntity(String identify, boolean markMornitorSingle) {
		this.identify = identify;
		pro = new ProgressEntity();
		String time = sim.format(new Date());
		String[] timeArr = time.split(";");
		this.date = timeArr[0];
		this.startTime = timeArr[1];
		this.markMornitorSingle = markMornitorSingle;
	}

	/** 起始时间 */
	private String startTime;

	/**
	 * 结束日期
	 */
	private String endDate;

	/** 结束时间 */
	private String endTime;

	private Integer result;

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	private String demo;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/** 日期 */
	private String date;
	/** 耗时 */
	private AtomicLong usedTime;
	/** 平均耗时 */
	private long avgTime;
	/** 成功的百分比 */
	private float succPercent;
	/** 成功条数 */
	private int succCount;
	/** 失败条数 */
	private int failCount;

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getSuccCount() {
		return succCount;
	}

	public void setSuccCount(int succCount) {
		this.succCount = succCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public long getUsedTime() {
		long usedTime = this.usedTime.get();
		return usedTime;
	}

	public void setUsedTime(long usedTime) {
		this.usedTime = new AtomicLong(usedTime);
	}

	public long getAvgTime() {
		return avgTime;
	}

	public float getSuccPercent() {
		return succPercent;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getDate() {
		return date;
	}

	public ProgressEntity getProgress() {
		return pro;
	}

	public void MakeDB() {
		this.pro.MakeDB();
	}

	/**
	 * 用于监控的工具类
	 *
	 * @author leeizhang
	 *
	 */
	public class ProgressEntity {

		/** 存储成功和失败的数量 true:成功 false:失败 */
		private ConcurrentHashMap<Boolean, Integer> succAndFailCountMap = null;

		public ProgressEntity() {
			succAndFailCountMap = new ConcurrentHashMap<Boolean, Integer>();
		}

		/**
		 * 收集成功和失败的数量
		 *
		 * @param identity
		 * @param status
		 */
		public synchronized void Count(boolean status, int unit) {
			if (succAndFailCountMap.get(status) == null || markMornitorSingle) {
				succAndFailCountMap.put(status, 0);
			}
			int succCount = succAndFailCountMap.get(status);
			succCount += unit;
			succAndFailCountMap.put(status, succCount);
		}

		public void Count(boolean status) {
			Count(status, 1);
		}

		public boolean IsSingleMo() {
			return markMornitorSingle;
		}

		/**
		 * 收集抓取并解析的耗时
		 *
		 * @param time
		 */
		public synchronized void AddTime(long time) {
			if (usedTime == null || markMornitorSingle) {
				usedTime = new AtomicLong(0l);
			}
			usedTime.addAndGet(time);
		}

		public void MakeDB() {
			if (succAndFailCountMap.get(true) != null) {
				succCount = succAndFailCountMap.get(true);
			}
			if (succAndFailCountMap.get(false) != null) {
				failCount = succAndFailCountMap.get(false);

			}
			int holeCount = succCount + failCount;
			if (holeCount != 0) {
				avgTime = usedTime.get() / holeCount;
			} else {
				avgTime = 0;
			}
			BigDecimal succBig = new BigDecimal(succCount);
			BigDecimal holeBig = new BigDecimal(holeCount);
			if (holeCount != 0) {
				succPercent = succBig.divide(holeBig, 2, BigDecimal.ROUND_HALF_UP).floatValue();
			} else {
				succPercent = 0;
			}
			if (MornitorEntity.this.identify != null) {
				// GnDao dao=null;
				// dao=new GnDaoImpl();
				// GnTbZbTaskMornitor mornitorEntity=null;
				// mornitorEntity=new GnTbZbTaskMornitor();
				// mornitorEntity.setIdentidy(MornitorEntity.this.identify);
				// mornitorEntity.setStartDate(MornitorEntity.this.date);
				// mornitorEntity.setStartTime(MornitorEntity.this.startTime);
				// mornitorEntity.setUseTime(usedTime.intValue());
				// mornitorEntity.setResult(result);
				// mornitorEntity.setDemo(demo);
				// mornitorEntity.setIp(getIp());
				// dao.insert(mornitorEntity);
			}
		}
	}

}
