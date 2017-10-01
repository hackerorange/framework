package com.ab.us.framework.core.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 获取Reids配置信息
 *
 * @author xusisheng
 * 2017-5-3 09:57:39
 *
 */
public class ReadRedisProperties {
	/**
	 * 采用静态方法
	 */
	private static Properties props = new Properties();

	static {
		try {
			props.load(ReadRedisProperties.class.getClassLoader().getResourceAsStream("redis.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取属性文件中相应键的值
	 *
	 * @param key
	 *            主键
	 * @return String
	 */
	public static String getString(String key) {
		return props.getProperty(key);
	}

	public static String getString(String key, String defaultVal) {
		String val = props.getProperty(key);
		if (val == null || val.equals("")) {
			val = defaultVal;
		}
		return val;
	}

	public static Integer getInteger(String key) {
		return getInteger(key, -1);
	}

	public static Integer getInteger(String key, Integer _default) {
		try {
			String val = props.getProperty(key);
			if ((val != null) && (!"".equals(val))) {
				return Integer.parseInt(val);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return _default;
	}

	public static Long getLong(String key) {
		return getLong(key, -1L);
	}

	public static Long getLong(String key, Long _default) {
		try {
			String val = props.getProperty(key);
			if ((val != null) && (!"".equals(val))) {
				return Long.parseLong(val);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return _default;
	}

	public static Boolean getBoolean(String key) {
		return getBoolean(key, Boolean.FALSE);
	}

	public static Boolean getBoolean(String key, Boolean _default) {
		try {
			String val = props.getProperty(key);
			if ((val != null) && (!"".equals(val))) {
				return Boolean.parseBoolean(val);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return _default;
	}
}
