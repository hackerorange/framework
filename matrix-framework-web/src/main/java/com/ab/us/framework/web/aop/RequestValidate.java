package com.ab.us.framework.web.aop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.web.annotation.FieldValidate;
import com.ab.us.framework.web.annotation.RequestModel;
import com.ab.us.framework.web.constant.GlobalExceptionInfo;
import com.ab.us.framework.web.constant.ValidateFeature;
import com.ab.us.framework.web.response.BodyResponse;

/**
 * @author Zhongchongtao
 */
@Aspect
@Component
public class RequestValidate {

	private Logger logger = LoggerFactory.getLogger( this.getClass() );

	@Before(value = "within(com.ab.us.framework.web.controller.BaseController+)")
	public void doBefore(JoinPoint joinPoint) {
		for (Object o : joinPoint.getArgs()) {
			if (!checkModel( o )) {
				throw BodyResponse.prepareException( GlobalExceptionInfo.MISSING_PARAM );
			}
		}
	}

	/**
	 * 校验model
	 *
	 * @param object
	 *            要校验的对象
	 * @return 是否校验通过
	 */
	private boolean checkModel(Object object) {
		if (TypeChecker.isNull( object )) {
			return true;
		}
		if (object.getClass().isAnnotationPresent( RequestModel.class )) {
			for (Field field : object.getClass().getDeclaredFields()) {
				if (!checkField( object, field )) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 校验字段
	 *
	 * @param object
	 *            要校验的对象
	 * @param field
	 *            要校验的字段
	 * @return 是否校验通过
	 */
	private boolean checkField(Object object, Field field) {
		if (field.isAnnotationPresent( FieldValidate.class )) {
			FieldValidate annotation = field.getAnnotation( FieldValidate.class );
			ValidateFeature[] validateFeatures = annotation.value();
			String name = field.getName();
			String methodName = "get" + name.substring( 0, 1 ).toUpperCase() + name.substring( 1 );
			Method method = ReflectionUtils.findMethod( object.getClass(), methodName );
			try {
				Object fieldValue = method.invoke( object );
				if (!TypeChecker.isEmpty( validateFeatures )) {
					for (ValidateFeature validateFeature : validateFeatures) {

						boolean validate = validateFeature.validate( fieldValue );
						if (!validate) {
							throw BodyResponse.prepareException( GlobalExceptionInfo.MISSING_PARAM, name );
						}
					}
				}
				// 递归检测集合中的对象是否需要校验
				if (fieldValue instanceof Collection) {
					// noinspection unchecked
					((Collection) fieldValue).forEach( this::checkModel );
				}
				// 校验属性是否需要校验
				checkModel( fieldValue );
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
