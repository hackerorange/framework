package com.ab.us.framework.db.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class BaseBean extends Object implements Serializable, BaseEntity {

	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String VALUE = "value";

	@Override
	public Object clone() {
		BaseEntity o = null;
		try {
			o = (BaseEntity)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 依据对象属性名，获取该属性值
	 *
	 * @param fieldName 属性名
	 * @param o 对象
	 * @return 属性值
	 */

	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取属性名数组
	 *
	 * @return 属性名数组
	 */
	@SuppressWarnings("unused")
	private String[] getFiledNames() {
		Field[] fields = getClass().getDeclaredFields();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < fields.length; i++){
			if (fields[i].getName().equals("serialVersionUID")) {
				continue;
			}
			list.add(fields[i].getName());
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 获取属性值数组
	 *
	 * @return 属性值数组
	 */
	@SuppressWarnings("unused")
	private Object[] getFiledValues() {
		Field[] fields = getClass().getDeclaredFields();
		Object[] fieldValues = new Object[fields.length];
		for (int i = 0; i < fields.length; i++){
			//System.out.println(getFieldValueByName(fields[i].getName(), this));
			fieldValues[i] =getFieldValueByName(fields[i].getName(), this);
		}
		return fieldValues;
	}

	/**
	 * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
	 * 不包含无get接口的属性及为赋值的属性
	 *
	 * @return 对象属性列表
	 */
	public List<Map<String, Object>> toListValid() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Field[] fields = getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Object v = getFieldValueByName(fields[i].getName(), this);
			if ((v == null) || (v.toString().equals("")) || (v.toString().equals(" "))) {
				continue;
			}
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put(TYPE, fields[i].getType().toString());
			infoMap.put(NAME, fields[i].getName());
			infoMap.put(VALUE, getFieldValueByName(fields[i].getName(), this));
			list.add(infoMap);
		}
		return list;
	}

}
