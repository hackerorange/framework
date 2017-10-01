package com.ab.us.framework.core.interpreter.paramHolder;

import com.ab.us.framework.core.utils.TypeChecker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhongchongtao
 */
public class ExtraParamParamHolder implements TranslateParamHolder<String, String> {


    private Map<String, String> extraParams;

    public void addParam(String param, String value) {
        if (TypeChecker.isNull(extraParams)) {
            extraParams = new HashMap<>();
        }
        extraParams.put(param, value);
    }

    @Override
    public String getParam(String s) {
        if (TypeChecker.isNull(extraParams)) {
            return null;
        }
        return extraParams.getOrDefault(s, null);
    }

    @Override
    public boolean containsKey(String s) {
        return !TypeChecker.isNull(extraParams) && extraParams.containsKey(s);
    }
}
