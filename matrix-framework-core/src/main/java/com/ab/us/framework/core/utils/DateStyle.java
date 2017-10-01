package com.ab.us.framework.core.utils;

public enum DateStyle {
	//英文格式的时间转换时需要带上Locale.ENGLISH
	//a. 时间格式: “2015-08-28”， 模式: “yyyy-MM-dd”
	//b. 时间格式: “2015-08-28 18:28:30”， 模式: “yyyy-MM-dd HH:mm:ss”
	//c. 时间格式: “2015-8-28”， 模式: “yyyy-M-d”
	//d. 时间格式: “2015-8-28 18:8:30”， 模式: “yyyy-M-d H:m:s”
	//e. 时间格式: “Aug 28, 2015 6:8:30 PM”， 模式: “MMM d, yyyy h:m:s aa”
	//f. 时间格式: “Fri Aug 28 18:08:30 CST 2015”， 模式: “EEE MMM d HH:mm:ss ‘CST’ yyyy”

	YYYY_MM("yyyy-MM", false),
	YYYY_MM_DD("yyyy-MM-dd", false),
	YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
	YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),

	YYYY_MM_EN("yyyy/MM", false),
	YYYY_MM_DD_EN("yyyy/MM/dd", false),
	YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),
	YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

	YYYY_MM_CN("yyyy年MM月", false),
	YYYY_MM_DD_CN("yyyy年MM月dd日", false),
	YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),
	YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),

	YYYYMM("yyyyMM", false),
	YYYYMMDD("yyyyMMdd", false),
	MMDD("MMdd", false),

	HH_MM("HH:mm", true),
	HH_MM_SS("HH:mm:ss", true),

	MM_DD("MM-dd", true),
	MM_DD_HH_MM("MM-dd HH:mm", true),
	MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

	MM_DD_EN("MM/dd", true),
	MM_DD_HH_MM_EN("MM/dd HH:mm", true),
	MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),

	MM_DD_CN("MM月dd日", true),
	MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),
	MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true);

	private String value;

	private boolean isShowOnly;

	DateStyle(String value, boolean isShowOnly) {
		this.value = value;
		this.isShowOnly = isShowOnly;
	}

	public String getValue() {
		return value;
	}

	public boolean isShowOnly() {
		return isShowOnly;
	}
}