package com.ab.us.framework.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 字符串处理
 *
 * @author xusisheng
 * @date 2017-02-09 09:46:50
 */
public class StringUtil {

	public static final String SINGLE_SPLIT_LINE_STRING = "----------------------------------------------------------------------------------------------------";
	public static final String DOUBLE_SPLIT_LINE_STRING = "====================================================================================================";
	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
	private static final String DEFAULT_SPLIT = ",";
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_QUOTATION_MARKS = "'";

	private static final String[] EMPTY_STRING_ARRAY = new String[] {};

	/**
	 * 将 String 转换为 application/x-www-form-urlencoded MIME 格式的串
	 *
	 * @param file_path 要转换的目标的字符串，GBK格式
	 * @return 以UTF-8编码的字符串
	 */
	public static String URLEncoder(String file_path) {
		String wwwurl = "";
		try {
			wwwurl = URLEncoder.encode(file_path, "UTF-8");
		} catch (UnsupportedEncodingException e) {

		}
		return wwwurl;
	}

	public static String getDir(String dir) {
		if (dir == null || "".equals(dir)) {
			return "";
		}
		dir = dir.trim();
		if ("/".equals(dir)) {
			return "/";
		}
		if (dir.endsWith("/")) {
			dir = new StringBuilder(dir).deleteCharAt(dir.length() - 1).toString();
		}
		return dir;
	}

	public static String[] getDirAndFileName(String dir) {

		String[] dAndF = new String[2];
		// 处理 / 的情况
		if ("/".equals(dir)) {
			dAndF[0] = "";
			dAndF[1] = "/";
			return dAndF;
		}
		// 拿到父目录
		String parentDir = dir.substring(0, dir.lastIndexOf("/") + 1);
		parentDir = parentDir == null ? "" : parentDir.trim();
		// 处理 /a/b/或/a/b 类似的情况
		if (!"/".equals(parentDir)) {
			if (parentDir.endsWith("/")) {
				parentDir = new StringBuilder(parentDir).deleteCharAt(parentDir.length() - 1).toString();
			}
		}
		dAndF[0] = parentDir;
		// 拿到目录
		String fileName = dir.substring(dir.lastIndexOf("/") + 1);
		fileName = fileName == null ? "" : fileName.trim();
		dAndF[1] = fileName;
		return dAndF;

	}

	/**
	 * 将 String 从 application/x-www-form-urlencoded MIME 格式解码为UTF8格式的字符串
	 *
	 * @param wwwurl 要转换的目标的字符串，application/x-www-form-urlencoded MIME 格式
	 * @return UTF8格式的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String URLDecoder(String wwwurl) {
		String filepath_new = "";
		try {
			filepath_new = URLDecoder.decode(wwwurl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return filepath_new;
	}

	/**
	 * 正则检查
	 *
	 * @param reg
	 * @param string
	 * @return
	 */
	public static boolean regCheck(String reg, String string) {
		boolean tem = false;
		try {
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(string);
			tem = matcher.matches();
		} catch (Exception se) {
			se.printStackTrace();
		}
		return tem;
	}

	/**
	 * 添加手机号检查，检查手机号是否正确
	 *
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		boolean b = false;
		Pattern p = Pattern.compile("^[1][3,4,5,6,7,8][0-9]{9}$"); // 验证手机号
		Matcher m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public static String toStringWithNull(Object object) {
		if (object != null) {
			return object.toString();
		}
		return null;
	}

	public static String toStringWithBlank(Object object) {
		if (object != null) {
			return object.toString();
		}
		return "";
	}

	public static Integer toIntegerWithNull(Object object) {
		if (object != null) {
			if (org.apache.commons.lang3.StringUtils.isNumeric(object.toString())) {
				return Integer.parseInt(object.toString());
			}
		}
		return null;
	}

	public static Integer toIntegerWithZero(Object object) {
		if (object != null) {
			if (org.apache.commons.lang3.StringUtils.isNumeric(object.toString())) {
				return Integer.parseInt(object.toString());
			}
		}
		return 0;
	}

	public static byte[] getBytes(String str, String encoding) {
		try {
			return str.getBytes(encoding);
		} catch (UnsupportedEncodingException ex) {
			logger.error("", ex);
		}
		return null;
	}

	public static List<String> str2List(String str) {
		return str2List(str, DEFAULT_SPLIT);
	}

	/**
	 * 将以<b>split</b>分隔的字符串转换为列表
	 *
	 * @param str 待转换的字符串
	 * @param split 字符串分隔符
	 * @return
	 */
	private static List<String> str2List(String str, String split) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
			return Collections.emptyList();
		}
		String[] strArray = str.split(split);
		return Arrays.asList(strArray);
	}

	/**
	 * 将字符串列表转换为以逗号分隔的字符串
	 *
	 * @param strList 字符串列表
	 * @return
	 */
	public static String list2str(List<String> strList) {
		StringBuilder resultBuffer = new StringBuilder();
		if (strList == null || strList.size() == 0) {
			return EMPTY_STRING;
		}
		for (Iterator<String> iterator = strList.iterator(); iterator.hasNext();) {
			resultBuffer.append(",").append(iterator.next());
		}
		return resultBuffer.toString().substring(1);
	}

	/**
	 * 将字符串列表转换为以逗号分隔的字符串
	 *
	 * @param strList 字符串列表
	 * @return
	 */
	public static String list2str(List<String> strList, String split) {
		StringBuilder resultBuffer = new StringBuilder();
		if (strList == null || strList.size() == 0) {
			return EMPTY_STRING;
		}
		for (Iterator<String> iterator = strList.iterator(); iterator.hasNext();) {
			resultBuffer.append(split).append(iterator.next());
		}
		return resultBuffer.toString().substring(1);
	}

	/**
	 * 将字符串数组转换为以逗号分隔的字符串
	 *
	 * @param array 字符串数组
	 * @return
	 */
	public static String arr2str(String[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_STRING;
		}
		return list2str(Arrays.asList(array));
	}

	/**
	 * @param list
	 * @return
	 */
	public static String[] list2Array(List<String> list) {
		if (list == null || list.size() == 0) {
			return EMPTY_STRING_ARRAY;
		}
		return list.toArray(new String[] {});
	}

	/**
	 * 将以ISO-8859-1编码的字符串转换为GBK编码
	 *
	 * @param isostr 采用ISO编码的字符串
	 * @return
	 */
	public static String iso2gbk(String isostr) {
		return convStrEncode(isostr, "ISO-8859-1", "GBK");
	}

	/**
	 * 将以GBK编码的字符串转换为以UTF-8编码的字符串
	 *
	 * @param gbkstr 采用GBK编码的字符串
	 * @return
	 */
	public static String convGBK2UTF8(String gbkstr) {
		return convStrEncode(gbkstr, "GBK", "UTF-8");
	}

	public static String bytes2str(byte[] bytes, String charset) {
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException ex) {
			logger.error("", ex);
		}
		return EMPTY_STRING;
	}

	/**
	 * @param str 待编码转换的字符串
	 * @param srcCharset 原字符串编码
	 * @param destCharset 目标字符串编码
	 * @return
	 */
	public static String convStrEncode(String str, String srcCharset, String destCharset) {
		try {
			String result = new String(str.getBytes(srcCharset), destCharset);
			return result;
		} catch (UnsupportedEncodingException ex) {
			logger.error("==字符转码出现异常==", ex);
		}
		return EMPTY_STRING;
	}

	public static Long str2LongWrapper(String str, Long defaultValue) {
		Long _long = defaultValue;
		try {
			_long = Long.valueOf(str);
		} catch (NumberFormatException ex) {
			// noop
		}
		return _long;
	}

	public static Long str2LongWrapper(String str) {
		return str2LongWrapper(str, null);
	}

	public static long str2Long(String str) {
		return str2Long(str, -1);
	}

	public static Integer str2Integer(String str, Integer defaultValue) {
		Integer _integer = defaultValue;
		try {
			_integer = Integer.valueOf(str);
		} catch (NumberFormatException ex) {
			// noop
		}
		return _integer;
	}

	public static Integer str2Integer(String str) {
		return str2Integer(str, null);
	}

	/**
	 * 将给定的字符串转换为int，使用默认值-1
	 *
	 * @param str
	 * @return
	 * @see #str2Int(String, int)
	 */
	public static int str2Int(String str) {
		return str2Int(str, -1);
	}

	/**
	 * 将str转换为int，如果转换异常则使用默认值
	 *
	 * @param str 转换字符串
	 * @param defaultValue 默认值
	 * @return
	 */
	public static int str2Int(String str, int defaultValue) {
		if (org.apache.commons.lang3.StringUtils.isBlank(str))
			return defaultValue;
		int _int = defaultValue;
		try {
			_int = Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			// noop
		}
		return _int;
	}

	/**
	 * 将str转换为long，如果转换异常使用默认值
	 *
	 * @param str 转换字符串
	 * @param defaultValue 默认值
	 * @return
	 */
	public static long str2Long(String str, long defaultValue) {
		if (org.apache.commons.lang3.StringUtils.isBlank(str))
			return defaultValue;
		long _long = defaultValue;
		try {
			_long = Long.parseLong(str);
		} catch (NumberFormatException ex) {
			// noop
		}
		return _long;
	}

	/**
	 * @param format 字符串格式
	 * @param args 替换的参数值
	 * @return
	 */
	public static String buildString(String format, Object... args) {
		Matcher matcher = RegexUtils.matcher("(\\{\\d\\})", format);
		String result = format;
		if (args == null) {
			return result;
		}
		while (matcher.find()) {
			String token = matcher.group();
			int idx = Integer.parseInt(token.substring(1, token.length() - 1));
			result = result.replace(token, args[idx] == null ? "" : args[idx].toString());
		}
		return result;
	}

	/**
	 * 字符串连接方法
	 *
	 * @param args
	 * @return
	 */
	public static String concat(Object... args) {
		if (args == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			sb.append(arg);
		}
		return sb.toString();
	}

	/**
	 * 模仿c语言中的sprintf实现，采用jdk1.5的格式化输出printf方式实现
	 *
	 * @param format 字符串格式
	 * @param args
	 * @return
	 */
	public static String sprintf(String format, Object... args) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		out.printf(format, args);
		out.close();
		return writer.toString();
	}

	public static String toSqlInString(String str) {
		if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
			return str;
		}
		String[] strArray = str.split(DEFAULT_SPLIT);
		StringBuilder stringBuilder = new StringBuilder();
		for (String _string : strArray) {
			if (_string.startsWith(SINGLE_QUOTATION_MARKS) && _string.endsWith(SINGLE_QUOTATION_MARKS)) {
				stringBuilder.append(DEFAULT_SPLIT).append(_string);
			} else {
				stringBuilder.append(DEFAULT_SPLIT);
				stringBuilder.append(SINGLE_QUOTATION_MARKS).append(_string).append(SINGLE_QUOTATION_MARKS);
			}
		}
		return stringBuilder.substring(1);
	}

	public static boolean isSecondLevelDomain(String arg0) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(arg0))
			return false;
		return match(arg0, "[a-zA-Z0-9_][a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+){2,}");
	}

	public static boolean match(String arg, String pattern) {
		Pattern pat = Pattern.compile(pattern);
		Matcher matcher = pat.matcher(arg);
		return matcher.matches();
	}

	public static Integer[] stringArray2IntegerArray(String[] stringArray) {
		return stringArray2IntegerArray(stringArray, -1);
	}

	public static Integer[] stringArray2IntegerArray(String[] stringArray, Integer defaultInteger) {
		if (stringArray == null || stringArray.length == 0) {
			return null;
		}
		Integer[] integerArray = new Integer[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			integerArray[i] = str2Integer(stringArray[i].trim(), defaultInteger);
		}
		return integerArray;
	}

	public static String str3Str(String str) {
		String s1 = String.format("%.3f", Double.valueOf(str) / 1000).replace(".", "_");
		return s1.substring(s1.indexOf("_") + 1, s1.length());
	}

	public static String str3Str(Integer in) {
		String s1 = String.format("%.3f", Double.valueOf(in) / 1000).replace(".", "_");
		return s1.substring(s1.indexOf("_") + 1, s1.length());
	}

	public static void main(String[] args) {
	}

	/**
	 * 判断两个字符串是否相等 如果都为null则判断为相等,一个为null另一个not null则判断不相等 否则如果s1=s2则相等
	 *
	 * @param s1 第一个字符串
	 * @param s2 第二个字符串
	 * @return 相等则为true否则为false
	 */
	public static boolean equals(String s1, String s2) {
		if (isEmpty(s1) && isEmpty(s2)) {
			return true;
		} else if (!org.apache.commons.lang3.StringUtils.isEmpty(s1) && !org.apache.commons.lang3.StringUtils.isEmpty(s2)) {
			return s1.equals(s2);
		}

		return false;
	}

	/**
	 * 判断字符串是否非空，如果空，返回默认值
	 *
	 * @param source 要判断的字符串
	 * @param defaultString 默认值
	 * @return 如果非空，返回原始值，否则，返回默认值
	 */
	public static String getValueByDef(String source, String defaultString) {
		return TypeChecker.isEmpty(source) ? defaultString : source;
	}

	/**
	 * 判断是否所有的对象都不为空
	 *
	 * @param source 要判断的所有对象那
	 * @return 是否为空
	 */
	public static boolean isAllNotEmpty(String... source) {
		if (TypeChecker.isNull(source)) {
			return false;
		}
		for (String s : source) {
			if (isEmpty(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param source 字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(String source) {
		return source == null || source.equals("");
	}

	/**
	 * 首字母转大写
	 *
	 * @param source 要转化的字符串
	 * @return 首字母大写后的结果
	 */
	public static String upperStartChar(String source) {

		if (TypeChecker.isEmpty(source)) {
			return source;
		}

		if (source.length() == 1) {
			return source.toUpperCase();
		}

		return source.substring(0, 1).toUpperCase() + source.substring(1, source.length());
	}

	public static String replaceAll(String source, String key, String value) {
		return org.apache.commons.lang.StringUtils.replace(source, key, value);
	}

	public static String[] splitBySeparator(String source, String separator) {

		return splitBySeparator(source, separator, true);

	}

	public static String[] splitBySeparator(String source, String separator, boolean blankIncluded) {
		if (source == null) {
			return ObjectUtils.EMPTY_STRING_ARRAYS;
		}
		if (separator == null || separator.length() == 0) {
			return (new String[] { source });
		}

		int startIndex = 0;

		int endIndex = source.indexOf(separator);

		List<String> items = new ArrayList<>();

		for (; endIndex >= startIndex; endIndex = source.indexOf(separator, startIndex)) {

			String value = source.substring(startIndex, endIndex);

			if (!TypeChecker.isEmpty(value) || blankIncluded) {
				items.add(org.apache.commons.lang3.StringUtils.trimToEmpty(value));
			}

			startIndex = endIndex + separator.length();
		}

		String value = source.substring(startIndex);

		if (!TypeChecker.isEmpty(value) || blankIncluded) {
			items.add(org.apache.commons.lang3.StringUtils.trimToEmpty(value));
		}

		return items.toArray(new String[items.size()]);
	}

	public static String[] splitBySeparators(String source, String... separators) {

		if (TypeChecker.isNull(source)) {
			return ObjectUtils.EMPTY_STRING_ARRAYS;
		}

		Assert.noNullElements(separators, "Please provide separators!");

		String useDefSeparator = separators[0];

		for (String sep : separators) {
			if (useDefSeparator.equalsIgnoreCase(sep)) {
				continue;
			}
			source = org.apache.commons.lang3.StringUtils.replace(source, sep, useDefSeparator);

		}

		return splitBySeparator(source, useDefSeparator);
	}

	/**
	 * Splits this string around matches of the given closure regular expression.
	 * The string "https://websurveyor.netpaction=resume&amp;index=0&amp;
	 * ID=*{userId}&amp;wsb42=*{userId}&amp;wsb41={getCurrentDateString(yyyy-MM-dd
	 * HH:mm:ss.SSS)}", for example, yields the following results with these
	 * expressions: regular expression: '{' -- start flag and '}' -- end flag return
	 * [userid, getDate(YYYY-MM-DD)]
	 *
	 * @param starFlag - the start regular expression
	 * @param endFlag - the end regular expression
	 * @return the array of strings computed by splitting this string around matches
	 *         of the given regular expression
	 */
	public static String[] splitClosure(String value, String starFlag, String endFlag) {

		if (TypeChecker.isEmpty(value)) {
			return null;
		}

		int start = value.indexOf(starFlag);

		if (start < 0) {
			return new String[] { value };
		}
		int end = value.indexOf(endFlag, start + starFlag.length());
		if (end < 0) {
			return new String[] { value };
		}

		List rs = new ArrayList();

		String temp = "";

		while (start >= 0 && end >= 0) {

			temp = value.substring(start + starFlag.length(), end);

			rs.add(org.apache.commons.lang3.StringUtils.trimToEmpty(temp));

			value = value.substring(end + endFlag.length());

			start = value.indexOf(starFlag);

			end = value.indexOf(endFlag, start + starFlag.length());
		}

		return (String[]) rs.toArray(new String[rs.size()]);

	}

	/**
	 * 随即生成指定位数的含数字验证码字符串
	 *
	 * @param bit 指定生成验证码位数
	 * @return String
	 */
	public static String randomNum(int bit) {
		if (bit == 0)
			bit = 6; // 默认6位
		String str;
		str = "0123456789";// 初始化种子
		return RandomStringUtils.random(bit, str);// 返回6位的字符串
	}

	/**
	 * 随即生成指定位数的含验证码字符串
	 *
	 * @param bit 指定生成验证码位数
	 * @return String
	 */
	public static String random(int bit) {
		if (bit == 0)
			bit = 6; // 默认6位
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str;
		str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";// 初始化种子
		return RandomStringUtils.random(bit, str);// 返回6位的字符串
	}

	public static String replaceByIndex(String source, String... extraParam) {
		Matcher matcher = Pattern.compile("\\{([0-9]+)}").matcher(source);
		while (matcher.find()) {
			String param = matcher.group();
			int index = Integer.parseInt(matcher.group(1));
			if (index < extraParam.length) {
				source = source.replace(param, extraParam[index]);
			} else {
				source = source.replace(param, "");
			}
		}
		return source;
	}

	public static String repeatChars(String chars, int length) {
		if (length < 0) {
			return chars;
		}
		StringBuffer temp = new StringBuffer(chars.length() * length);
		for (int i = 0; i < length; i++) {
			temp.append(chars);
		}
		return temp.toString();
	}

	public static String appendBeforeCharToLength(String src, String chars, int length) {
		if (length <= 0) {
			return src;
		}
		if (TypeChecker.isEmpty(src)) {
			return repeatChars(chars, length);
		}
		while (src.length() < length) {
			src = chars + src;
		}
		return src;
	}

	public static String appendBehindCharToLength(String src, String chars, int length) {
		if (length <= 0) {
			return src;
		}
		if (TypeChecker.isEmpty(src)) {
			return repeatChars(chars, length);
		}
		while (src.length() < length) {
			src += chars;
		}
		return src;
	}

	public static byte[] hexStrToBytes(String HexString) {
		int numberChars = HexString.length();
		byte[] bytes = new byte[numberChars / 2];
		for (int i = 0; i < numberChars; i += 2) {
			bytes[i / 2] = (byte) (Integer.parseInt(HexString.substring(i, i + 2), 16));
		}
		return bytes;
	}

	public static String byteToHexStr(short in) {

		/* Lookup table for converting Hex byte values into chars */
		final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		StringBuffer s = new StringBuffer(4);

		s.append(bcdLookup[(in >>> 4) & 0x0f]);
		s.append(bcdLookup[in & 0x0f]);
		return s.toString();
	}

	public static String bytesToHexStr(byte[] in) {

		if (in.length == 0)
			return "NULL ";

		StringBuffer s = new StringBuffer(in.length * 5);
		for (int i = 0; i < in.length; i++) {
			s.append(byteToHexStr(in[i]));
		}
		return s.toString();
	}
}
