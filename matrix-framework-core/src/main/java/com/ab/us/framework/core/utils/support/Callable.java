package com.ab.us.framework.core.utils.support;

import org.apache.commons.lang3.StringUtils;

public interface Callable<T> {

    public static final Callable NONE = (value, arguments) -> value;
    public static final Callable<String> UPPERCASE = new Callable<String>() {
        @Override
        public String call(String value, Object... arguments) {
            return StringUtils.trimToEmpty(value).toUpperCase();
        }
    };
    public static final Callable<String> LOWERCASE = (value, arguments) -> StringUtils.trimToEmpty(value).toLowerCase();

    public T call(T value, Object... arguments);

}
