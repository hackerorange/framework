
package com.ab.us.framework.core.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

public class DateConverter implements Converter {

	@Override
	public Object convert(Class type, Object value){
        if(value == null){
            return null;
        }else if(type == Timestamp.class){
            return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss");
        }else if(type == Date.class){
            return convertToDate(type, value, "yyyy-MM-dd HH:mm:ss");
        }else if(type == String.class){
            return convertToString(type, value);
        }

        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
    }


	protected Object convertToDateInfo(Class type, Object value, String pattern) {
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if(value instanceof String){
            try{
            	if(StringUtils.isEmpty(value.toString())){
                	return null;
                }
                Date date = sdf.parse((String) value);
                if(type.equals(Timestamp.class)){
                	Timestamp timeStamp = new Timestamp(date.getTime());
                	Object obj = null;
                	obj = sdf.format(timeStamp);
                	return obj;

                }
                return date;
            }catch(Exception pe){
                return null;
            }
        }else if(value instanceof Date){
        	Object obj = null;
        	obj = sdf.format(value);
        	return obj;
        }

        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
    }


    protected Object convertToDate(Class type, Object value, String pattern) {
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if(value instanceof String){
            try{
            	if(StringUtils.isEmpty(value.toString())){
                	return null;
                }
                Date date = sdf.parse((String) value);
                if(type.equals(Timestamp.class)){
                    return new Timestamp(date.getTime());
                }
                return date;
            }catch(Exception pe){
                return null;
            }
        }else if(value instanceof Date){
        	return value;
        }

        throw new ConversionException("不能转换 " + value.getClass().getName() + " 为 " + type.getName());
    }

    protected Object convertToString(Class type, Object value) {
        if(value instanceof Date){
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (value instanceof Timestamp) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }

            try{
                return sdf.format(value);
            }catch(Exception e){
                throw new ConversionException("日期转换为字符串时出错！");
            }
        }else{
            return value.toString();
        }
    }

}
