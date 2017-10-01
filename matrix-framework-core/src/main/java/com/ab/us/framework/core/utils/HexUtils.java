package com.ab.us.framework.core.utils;

@SuppressWarnings({"WeakerAccess", "StringBufferMayBeStringBuilder"})
public final class HexUtils {
	public static char[] HEXCHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	private HexUtils() {

	}

	/**
	 * 将数组转化成十六进制
	 *
	 * @param hexDigits
	 *            十六进制数组[0-9,A-F]
	 * @param blocks
	 *            二进制数组
	 * @return 转化后的十六进制String
	 */
	public static String toHex(char[] hexDigits, byte blocks[]) {
		StringBuffer digests = new StringBuffer(blocks.length * 4);
		for (byte b : blocks) {
			digests.append(toHex(hexDigits, b));
		}
		return digests.toString();
	}

	/**
	 * 将byte数组转化为十六进制
	 *
	 * @param blocks
	 *            二进制数组
	 * @return 转化后的十六进制
	 */
	public static String toHex(byte blocks[]) {
		return toHex(HEXCHARS, blocks);
	}

	/**
	 * 将16进制转换为二进制
	 *
	 * @param hexStr
	 *            十六进制[String]
	 * @return 二进制
	 */
	public static byte[] toBinary(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 将二进制数组转化成十六进制
	 * 
	 * @param hexDigits
	 *            二进制数
	 * @param b
	 * @return 十六进制
	 */
	private static String toHex(char[] hexDigits, byte b) {

		char[] chars = new char[2];
		chars[0] = hexDigits[(b >>> 4) & 0X0F];
		chars[1] = hexDigits[b & 0X0F];

		return String.valueOf(chars);
	}

	public static void main(String[] args) {
	}

}
