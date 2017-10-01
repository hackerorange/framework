package com.ab.us.framework.service.message;

public interface SmsValidateCodeProvider {

    /**
     * 判断是否允许允许发送短信
     *
     * @param did   设备ID
     * @param phone 手机号码
     * @return 允许的话，返回true，否则返回false或者抛出异常
     */
    public boolean isAllowSendValidateCode(Long did, String phone);

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    public String getValidateCode();

    /**
     * 获取短信验证码配置名称
     *
     * @return 配置名称
     */
    public String getConfigName();
}
