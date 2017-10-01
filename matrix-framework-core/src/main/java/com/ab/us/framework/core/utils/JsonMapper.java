package com.ab.us.framework.core.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class JsonMapper {
	private static final Logger logger = LoggerFactory.getLogger(JsonMapper.class);
	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		// 如果为空则不输出
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		// 对于空的对象转json的时候不抛出错误
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// 禁用序列化日期为timestamps
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// 禁用遇到未知属性抛出异常
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 视空字符传为null
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

		// 低层级配置
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		// 允许属性名称没有引号
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 允许单引号
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 取消对非ASCII字符的转码
		objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
	}

	/**
	 * 从任意java对象转换到json字符串
	 *
	 * @param object
	 *            待转换的java对象
	 * @return json字符串
	 */
	public static String toJson(Object object) {
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(object);
		} catch (Exception ex) {
			logger.error("from object to JSON error", ex);
		}
		return jsonString;
	}

	/**
	 * 将json字符串转换为指定的java对象，不包括泛型的集合类
	 *
	 * @param json
	 *            json字符串
	 * @param clazz
	 *            转换java目标类
	 * @return
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		T object = null;
		try {
			object = objectMapper.readValue(json, clazz);
			return object;
		} catch (Exception ex) {
			logger.error("from json to Object error", ex);
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(String json, TypeReference<T> typeReference) {
		T object = null;
		try {
			object = (T) objectMapper.readValue(json, typeReference);
		} catch (Exception ex) {
			logger.error("from json to Object error", ex);
		}
		return object;
	}

	/**
	 * 将json字符串转换为java.util.Map,键值类型都为java.lang.String
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, String> toMap(String json) {
		return toObject(json, new TypeReference<Map<String, String>>() {
		});
	}

	/**
	 * 将json字符串转换为java.util.Map实例，键为java.lang.String,值为java.lang.Object
	 *
	 * @param json
	 *            json字符串
	 * @return
	 */
	public static Map<String, Object> toObjectMap(String json) {
		return toObject(json, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * 转换为指定键值类型的泛型java.util.Map实例
	 *
	 * @param json
	 *            json字符串
	 * @param k
	 *            键目标类
	 * @param v
	 *            值目标类
	 * @return
	 */
	public static <K, V> Map<K, V> toGenericMap(String json, Class<K> k, Class<V> v) {
		return toObject(json, new TypeReference<Map<K, V>>() {
		});
	}

	@SuppressWarnings("rawtypes")
	public static <T> Collection<T> toCollection(String json, Class<? extends Collection> collectionType,
			Class<T> elementType) {
		try {
			return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(collectionType, elementType));
		} catch (Exception ex) {
			logger.error("from json to collection error", ex);
		}
		return null;
	}

	/**
	 * 对象转为Map<String,String>
	 *
	 * @param obj
	 *            对象
	 * @return
	 */
	public static Map<String, String> beanToMap(Object obj) {
		Map<String, String> params = new HashMap<String, String>(0);
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!"class".equals(name)) {
					Object o = propertyUtilsBean.getNestedProperty(obj, name);
					if (o != null) {
						params.put(name, propertyUtilsBean.getNestedProperty(obj, name).toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	public static <T> List<T> toList(String json, Class<T> t) {
		return (List<T>) toCollection(json, ArrayList.class, t);
	}

	public static ObjectMapper getMapper() {
		return objectMapper;
	}
}
