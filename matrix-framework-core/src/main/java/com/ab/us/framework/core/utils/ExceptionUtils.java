package com.ab.us.framework.core.utils;

import com.ab.us.framework.core.exception.BaseException;
import com.ab.us.framework.core.exception.support.ExceptionTranslator;
import com.ab.us.framework.core.spring.SpringApplicationContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class ExceptionUtils {

    private static final int TRY_INVOCATIONTARGET_COUNTS = 10;
    private static List<ExceptionTranslator> exceptionTranslators = null;

    private ExceptionUtils() {

    }

    public static BaseException convertThrowableToBaseException(Throwable ep) {
        int count = -1;
        while (count++ < TRY_INVOCATIONTARGET_COUNTS && ep instanceof InvocationTargetException) {
            ep = ((InvocationTargetException) ep).getTargetException();
        }
        if (ep instanceof BaseException) {
            return (BaseException) ep;
        }
        return translateException(ep);
    }

    private static BaseException translateException(Throwable ep) {

        if (!SpringApplicationContextHolder.isApplicationContextHolden()) {
            return new BaseException(ep);
        }

        injectExceptionTranslators();

        for (ExceptionTranslator exceptionTranslator : exceptionTranslators) {
            ep = exceptionTranslator.translateException(ep);
        }

        if (ep instanceof BaseException) {
            return (BaseException) ep;
        }

        return new BaseException(ep);

    }

    private static void injectExceptionTranslators() {

        if (!TypeChecker.isNull(exceptionTranslators)) {
            return;
        }

        String[] beanNames = SpringApplicationContextHolder.getApplicationContext().getBeanNamesForType(ExceptionTranslator.class);

        if (TypeChecker.isEmpty(beanNames)) {

            exceptionTranslators = ObjectUtils.EMPTY_LIST;

            return;
        }

        exceptionTranslators = new ArrayList<>(beanNames.length);

        for (String beanName : beanNames) {
            exceptionTranslators.add((ExceptionTranslator) SpringApplicationContextHolder.getBean(beanName));
        }
    }

}
