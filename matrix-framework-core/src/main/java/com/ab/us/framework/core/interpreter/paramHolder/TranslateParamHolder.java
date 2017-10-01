package com.ab.us.framework.core.interpreter.paramHolder;

/**
 * @author Zhongchongtao
 */
public interface TranslateParamHolder<T, R> {

    /**
     * 获取参数
     *
     * @param t 参数名称
     * @return 返回值
     */
    public R getParam(T t);

    /**
     * 是否包含参数
     *
     * @param t 参数名称
     * @return 是否包含此参数
     */
    public boolean containsKey(T t);

}
