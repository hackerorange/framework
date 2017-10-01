package com.ab.us.framework.core.utils;

import java.util.UUID;

/**
 * @author Zhongchongtao
 */
public class UUIDUtils {

    /**
     * 生成一个UUID
     *
     * @return 生成的UUID, 存在分隔符“-”<br/>
     * 如果需要去掉“-”,请使用：{@link #generate(Boolean)}方法，参数设置为true
     */
    public static String generate() {
        return generate(false);
    }

    /**
     * 生成一个UUID
     *
     * @param removeSplit 是否移除分隔符“-”
     * @return 生成的UUID
     */
    public static String generate(Boolean removeSplit) {
        return removeSplit ? UUID.randomUUID().toString().replace("-", "") : UUID.randomUUID().toString();
    }


}
