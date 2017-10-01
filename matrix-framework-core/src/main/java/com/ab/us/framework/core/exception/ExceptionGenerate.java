package com.ab.us.framework.core.exception;

/**
 * 异常生成器
 *
 * @author Zhongchongtao
 */
public interface ExceptionGenerate {

    /**
     * 生成基础异常信息
     *
     * @return 异常信息
     */
    public BaseException generateBaseException(String... strings);

}
