package com.ab.us.framework.core.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ClassUtils extends org.springframework.util.ClassUtils {

    private ClassUtils() {
    }

    /**
     * 查找basePackage包下面的所有的类
     *
     * @param basePackage 要查找的类所在的包
     * @return basePackage下的所有类
     */
    public static List<Class<?>> findAllClassUnderPackage(String basePackage) {
        List<Class<?>> list = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        // 加载系统所有类资源
        basePackage = basePackage.replace('.', '/');
        if (!basePackage.endsWith("/")) {
            basePackage = basePackage + "/";
        }
        try {
            Resource[] resources;
            resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackage + "**/**.*");
            // 把每一个class文件找出来
            for (Resource r : resources) {
                try {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(r);
                    Class<?> clazz = forName(metadataReader.getClassMetadata().getClassName(), ClassUtils.class.getClassLoader());
                    list.add(clazz);
                } catch (Throwable ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获取同一路径下所有子类或接口实现类
     *
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 取得当前类路径下的所有类
     *
     * @param cls
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getClasses(Class<?> cls) throws IOException, ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }

    /**
     * 迭代查找类
     *
     * @param dir
     * @param pk
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

    /**
     * 获取所有注释字段
     *
     * @param obj
     * @return
     */
    public static List<Field> getAnnoFieldList(Object obj, Class<? extends Annotation> annotationClass) {
        List<Field> list = new ArrayList<Field>();
        Class<?> superClass = obj.getClass().getSuperclass();
        while (true) {
            if (superClass != null) {
                Field[] superFields = superClass.getDeclaredFields();
                if (superFields != null && superFields.length > 0) {
                    for (Field field : superFields) {
                        if (field.isAnnotationPresent(annotationClass)) {
                            list.add(field);
                        }
                    }
                }
                superClass = superClass.getSuperclass();
            } else {
                break;
            }
        }
        Field[] objFields = obj.getClass().getDeclaredFields();
        if (objFields != null && objFields.length > 0) {
            for (Field field : objFields) {
                if (field.isAnnotationPresent(annotationClass)) {
                    list.add(field);
                }
            }
        }
        return list;
    }


    /**
     * 获取字段的值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = null;
        Class<?> claszz = obj.getClass();
        do {
            try {
                field = claszz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignore) {
                field = null;
            } catch (IllegalArgumentException ignore) {
                field = null;
            }
            claszz = claszz.getSuperclass();
        } while (field == null && claszz != null);

        if (field == null) {
            return null;
        }

        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    /**
     * 获取字段的值
     *
     * @param obj
     * @param field
     * @return
     */
    public static Object getFieldValue(Object obj, Field field) {
        return getFieldValue(obj, field.getName());
    }

    public static void setFieldVal(Object obj, String fieldName, Object val) throws Exception {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, val);
        } catch (Exception se) {
            throw new Exception(se);
        }
    }

    /**
     * 向属性 重新复赋值
     *
     * @param obj   javabean
     * @param field 属性
     * @param value 属性value
     */
    public static void setFieldValue(Object obj, Field field, String value) {

        String name = field.getName();     //需要调整的属性名称
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);    //拼接javabean中该属性的赋值方法

        try {
            Class<?> classType = obj.getClass();
            Method method = classType.getMethod(methodName, field.getType());
            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向属性 重新复赋值
     *
     * @param obj   javabean
     * @param field 属性
     * @param value 属性value
     */
    public static void setFieldValue(Object obj, Field field, Object value) {

        String name = field.getName();     //需要调整的属性名称
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);    //拼接javabean中该属性的赋值方法

        try {
            Class<?> classType = obj.getClass();
            Method method = classType.getMethod(methodName, field.getType());
            method.invoke(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
