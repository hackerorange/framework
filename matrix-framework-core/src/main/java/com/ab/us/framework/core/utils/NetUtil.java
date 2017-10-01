package com.ab.us.framework.core.utils;

import java.net.InetAddress;

/**
 * @ClassName: NetTools
 * @Description: TODO(网络相关工具类)
 * @author xusisheng
 * @date 2017年03月20日16:23:07
 *
 */
public class NetUtil {

	public static String getLocIp() {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			return ia.getHostAddress();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
