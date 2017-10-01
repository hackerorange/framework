package com.ab.us.framework.core.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings({"unused", "SimplifiableIfStatement", "Duplicates"})
public final class TypeChecker {

	private TypeChecker() {
	}

	public static boolean isEmptyObject(Object obj) {
		if (isNull(obj)) {
			return true;
		}
		if (obj instanceof Object[]) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof Collection) {
			return isEmpty((Collection) obj);
		}
		if (obj instanceof Map) {
			return isEmpty((Map) obj);
		}
		// String obj
		if (obj instanceof String) {

			return isEmpty((String) obj);
		} else {
			// some else case
			return false;
		}
	}

	public static boolean isEmptySQL(String sql) {
		if (!isNull(sql) && !isEmpty(sql)) {
			int i = 0;

			for (int j = sql.length(); i < j; ++i) {
				if (sql.charAt(i) != 37) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}

	public static boolean isEmpty(Object[] arrays) {
		return isNull(arrays) || arrays.length == 0;
	}

	public static boolean isEmptyStringArrays(String... arrays) {
		if (isEmpty(arrays)) {
			return true;
		} else {
			for (String item : arrays) {
				if (!isEmpty(item)) {
					return false;
				}
			}
			return true;
		}
	}

	public static boolean isAllStringArraysNotEmpty(String... arrays) {
		if (isEmpty(arrays)) {
			return false;
		} else {
			for (String item : arrays) {
				if (isEmpty(item)) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 判断给定的数组中，是否至少有一个不为null
	 *
	 * @param arrays
	 *            要判断的数组
	 * @return 是否至少有一个不为空
	 */
	public static boolean isStringArraysOnlyOneNotEmpty(String... arrays) {
		if (isEmpty(arrays)) {
			return false;
		} else {
			for (String item : arrays) {
				if (!isEmpty(item)) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean isEmpty(StringBuffer sb) {
		return isNull(sb) || sb.length() == 0;
	}

	public static boolean isEmpty(Collection collection) {
		return isNull(collection) || collection.isEmpty();
	}

	public static boolean isEmpty(Map map) {
		return isNull(map) || map.isEmpty();
	}

	public static boolean isEmpty(String str) {
		return isNull(str) || str.trim().length() == 0 || "null".equalsIgnoreCase(str);
	}

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isOrNull(Object... objs) {
		if (isNull(objs)) {
			return true;
		} else {
			for (Object obj : objs) {
				if (isNull(obj)) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean isTrue(String value) {
		return "true".equalsIgnoreCase(value);
	}

	public static boolean isSpecialTrue(String value) {
		return "true".equalsIgnoreCase(value) || "t".equalsIgnoreCase(value) || "a".equalsIgnoreCase(value)
				|| "y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
	}

	public static boolean isSpecialFalse(String value) {
		return "false".equalsIgnoreCase(value) || "f".equalsIgnoreCase(value) || "n".equalsIgnoreCase(value)
				|| "no".equalsIgnoreCase(value) || isEmpty(value);
	}

	public static boolean isFalse(String value) {
		return "false".equalsIgnoreCase(value) || isEmpty(value);
	}

	public static boolean notNull(Object object) {
		return !isNull(object);
	}

	public interface TypeMoreChecker {
		boolean validate(Object... var1);
	}
}
