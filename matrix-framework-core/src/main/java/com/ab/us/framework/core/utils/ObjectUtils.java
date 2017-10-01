package com.ab.us.framework.core.utils;


import org.apache.commons.lang.math.NumberUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "unchecked", "Duplicates"})
public final class ObjectUtils extends org.springframework.util.ObjectUtils {

    public static final String[] EMPTY_STRING_ARRAYS = new String[0];

    public static final Object[] EMPTY_OBJECT_ARRAYS = new Object[0];

    public static final Class[] EMPTY_CLASS_ARRAYS = new Class[0];

    public static final byte[] EMPTY_BYTE_ARRAYS = new byte[0];

    public static final File[] EMPTY_FILE_ARRAYS = new File[0];

    public static final int[] EMPTY_INT_ARRAYS = new int[0];

    public static final Object EMPTY_OBJECT = new Object();

    public static final Map EMPTY_MAP = Collections.EMPTY_MAP;

    public static final List EMPTY_LIST = Collections.EMPTY_LIST;

    public static final Set EMPTY_SET = Collections.EMPTY_SET;

    public static final Properties EMTPY_PROPERTIES = new Properties();

    public static final List<Class> WIDE_PRIMITIVE_CLASSES = new ArrayList<>();

    public static final Map<Class, Number> WIDE_BASIC_NUMBER_CLASSES = new HashMap<>();

    public static final List<Class> WIDE_PRIMITIVE_NUMBER_CLASSES = new ArrayList<>();

    static {

        WIDE_PRIMITIVE_CLASSES.add(Date.class);
        WIDE_PRIMITIVE_CLASSES.add(String.class);
        WIDE_PRIMITIVE_CLASSES.add(Number.class);
        WIDE_PRIMITIVE_CLASSES.add(BigDecimal.class);
        WIDE_PRIMITIVE_CLASSES.add(Timestamp.class);
        WIDE_PRIMITIVE_CLASSES.add(Time.class);
        WIDE_PRIMITIVE_CLASSES.add(Integer.class);
        WIDE_PRIMITIVE_CLASSES.add(Float.class);
        WIDE_PRIMITIVE_CLASSES.add(Long.class);
        WIDE_PRIMITIVE_CLASSES.add(Double.class);

        WIDE_BASIC_NUMBER_CLASSES.put(int.class, 0);
        WIDE_BASIC_NUMBER_CLASSES.put(double.class, 0);
        WIDE_BASIC_NUMBER_CLASSES.put(byte.class, 0);
        WIDE_BASIC_NUMBER_CLASSES.put(long.class, 0);
        WIDE_BASIC_NUMBER_CLASSES.put(float.class, 0);

        WIDE_PRIMITIVE_NUMBER_CLASSES.add(Integer.class);
    }

    private ObjectUtils() {

    }

    public static Class[] getClassArrays(Class... classes) {

        return classes;
    }

    public static boolean isWidePrimitive(Class clz) {
        return clz.isPrimitive() || WIDE_PRIMITIVE_CLASSES.contains(clz);
    }


    public static Object getPrimitiveNullValue(Class clz) {

        if (WIDE_BASIC_NUMBER_CLASSES.containsKey(clz)) {
            return WIDE_BASIC_NUMBER_CLASSES.get(clz);
        }

        return null;
    }

    public static boolean isBasicPrimitiveType(Class clz) {
        return WIDE_BASIC_NUMBER_CLASSES.containsKey(clz);
    }

    public static Object getPrimitiveValue(Class clz, Object value) {
        if (clz == int.class || clz == Integer.class) {
            return NumberUtils.toInt(String.valueOf(value));
        }

        if (clz == long.class || clz == Long.class) {
            return NumberUtils.toLong(String.valueOf(value));
        }

        if (clz == float.class || clz == Float.class) {
            return (float) NumberUtils.toDouble(String.valueOf(value));
        }

        if (clz == double.class || clz == Double.class) {
            return NumberUtils.toDouble(String.valueOf(value));
        }

        throw new IllegalArgumentException("Class type == " + clz.getName());
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public byte[] toByteArray(Object obj) {

        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(obj);
            oos.flush();
            byte[] bytes = bos.toByteArray();
            oos.close();
            bos.close();

            return bytes;

        } catch (Exception ex) {
            throw ExceptionUtils.convertThrowableToBaseException(ex);
        }

    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public Object toObject(byte[] bytes) {

        try {

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            ois.close();
            bis.close();

            return obj;

        } catch (Exception ex) {
            throw ExceptionUtils.convertThrowableToBaseException(ex);
        }

    }

}
