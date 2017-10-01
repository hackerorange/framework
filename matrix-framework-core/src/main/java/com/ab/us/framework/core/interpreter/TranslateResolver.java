package com.ab.us.framework.core.interpreter;

import com.ab.us.framework.core.interpreter.paramHolder.TranslateParamHolder;

import java.util.List;

/**
 * Created by ZhongChongtao on 2017/4/12.
 */
public interface TranslateResolver<T, R> {
    /**
     * 转化，将param翻译后返回
     *
     * @param source    要翻译的对象
     * @param paramList 参数holder列表
     * @return 翻译后的结果
     */
    public R translate(T source, List<TranslateParamHolder<T, R>> paramList);

}
