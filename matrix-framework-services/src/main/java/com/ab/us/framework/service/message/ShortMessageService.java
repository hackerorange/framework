package com.ab.us.framework.service.message;

import java.util.Map;

/**
 * @author Zhongchongtao
 */
public interface ShortMessageService {

//    /**
//     * @param module       模板(业务模块，通过业务模块，获取短信校验码)
//     * @param phoneNumber  手机号码
//     * @param validateCode 校验码
//     * @param params       模板替换参数
//     * @param did          设备ID
//     * @param expiredTime  短信过期时间
//     * @return 短信是否发送成功
//     */
//    public boolean sendValidateShortMessageByTemplate(String module, String phoneNumber, String validateCode, Map<String, String> params, long did, long expiredTime);
//
    /**
     * 发送新的验证码
     *
     * @param provider 验证码提供者
     * @param did      设备ID
     * @param mobile   手机号码
     * @return 是否发送成功
     */
    public boolean sendValidateShortMessage(SmsValidateCodeProvider provider, Long did, String mobile, Map<String, String> params);


    /**
     * 校验提供的任务验证码是否是此用户的
     *
     * @param provider     模板(用于确定是哪个模块的校验码)
     * @param phoneNumber  手机号码
     * @param did          设备ID
     * @param validateCode 手机校验码
     * @return 是否校验通过
     */
    public boolean validateShortMessageCheckCode(SmsValidateCodeProvider provider, String phoneNumber, Long did, String validateCode);
}
