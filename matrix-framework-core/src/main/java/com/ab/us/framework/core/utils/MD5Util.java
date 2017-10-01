package com.ab.us.framework.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密
 * @author xusisheng
 * @date 2017-02-09 09:45:42
 *
 */
public class MD5Util {

	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);

	public static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	protected static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("MessageDigest: ", e);
			e.printStackTrace();
		}
	}

	/**
	 * MD5加密文件
	 *
	 * @param filePath
	 * @return
	 */
	public static String getMD5FromFile(String filePath) {
		FileInputStream fis = null;
		String md5Text = null;
		File file = null;
		try {
			file = new File(filePath);
			fis = new FileInputStream(file);
			int length = -1;
			byte[] buffer = new byte[2048];
			while ((length = fis.read(buffer)) != -1) {
				messagedigest.update(buffer, 0, length);
			}
			md5Text = byteToHex(messagedigest.digest());
		} catch (Exception e) {
			logger.error("getMD5FromFile:", e);
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return md5Text;
	}

	/**
	 * MD5加密字符串
	 *
	 * @param filePath
	 * @return
	 */
	public static String getMD5FromString(String str) {
		String md5Text = null;
		try {
			messagedigest.update(str.getBytes());
			md5Text = byteToHex(messagedigest.digest());
		} catch (Exception e) {
			logger.error("getMD5FromString:", e);
			e.printStackTrace();
		}
		return md5Text;
	}

	/**
	 * MD5加密以byte数组表示的字符串
	 *
	 * @param bytes
	 * @return
	 */
	private static String byteToHex(byte bytes[]) {
		return byteToHex(bytes, 0, bytes.length);
	}

	private static String byteToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
			char c1 = hexDigits[bytes[l] & 0xf];
			stringbuffer.append(c0);
			stringbuffer.append(c1);
		}
		return stringbuffer.toString();
	}

	public static void main(String[] args) {
		// String md5 = getMD5FromFile("D:/HBS_ACTIVE_INCREMENT_20110918.ZIP");
		// e10adc3949ba59abbe56e057f20f883e
		// [null, 0, 0, LBS003, MobileUsersMsg [pin=13596802622,
		// authCode=c96f7c9351826c35a493dcd4ae9fc343, systime=20130628153851]

		String md5 = getMD5FromString("15911091781" + "20130629194609" + "e10adc3949ba59abbe56e057f20f883e");
		// String md5 =getMD5FromString("123456");
		// 16be92189d5146b60f5c47bbe3fc4bb9

		System.out.println(md5);
	}
}
