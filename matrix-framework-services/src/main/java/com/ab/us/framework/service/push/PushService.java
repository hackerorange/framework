package com.ab.us.framework.service.push;

import com.ab.us.framework.service.push.constant.*;

/**
 * Created by ZhongChongtao on 2017/5/8.
 */
@SuppressWarnings("unused")
public interface PushService {
    /**
     * 通过tokenId推送消息
     *
     * @param deviceTokenId  用户设备token
     * @param devicePlatform 推送平台 {@link DevicePlatform 设备平台枚举}
     * @param timeout        超时时间,默认值设置为0
     * @param title          推送标题
     * @param level          推送有限级别，默认值设置为0
     * @param description    推送描述
     * @param customContent  额外参数
     * @param messageType    消息类型
     * @param pushCategory   消息分类
     * @param biz            业务模块
     * @return 是否推送成功
     */
    public boolean pushByTokenId(String deviceTokenId, DevicePlatform devicePlatform, String title, String description, Object customContent, MessageType messageType, PushCategory pushCategory, String biz, int timeout, int level);

    /**
     * 通过accountId推送消息
     *
     * @param accountId     accountId
     * @param timeout       超时时间,默认值设置为0
     * @param title         推送标题
     * @param level         推送有限级别,默认值设置为0
     * @param description   推送描述
     * @param customContent 额外参数
     * @param messageType   消息类型
     * @param pushCategory  消息分类
     * @param biz           业务模块
     * @return 是否推送成功
     */
    public boolean pushByAccountId(String accountId, String title, String description, Object customContent, MessageType messageType, PushCategory pushCategory, String biz, int timeout, int level);


    /**
     * 通过用户四元信息推送消息
     *
     * @param name          用户名
     * @param mobile        用户手机号
     * @param certType      证件类型
     * @param certCode      证件号码
     * @param timeout       超时时间
     * @param title         推送标题
     * @param level         推送有限级别
     * @param description   推送描述
     * @param customContent 额外参数
     * @param messageType   消息类型
     * @param pushCategory  消息分类
     * @param biz           业务模块
     * @return 是否推送成功
     */

    boolean pushByUserInfo(String name, String mobile, CertType certType, String certCode, String title, String description, Object customContent, MessageType messageType, PushCategory pushCategory, String biz, int timeout, int level);

    /**
     * 根据用户ID,向指定的用户发送短消息
     *
     * @param mobileNumber    短消息收信人手机号码
     * @param message         发送的消息
     * @param timeout         过期时间，默认设置为0
     * @param level           优先级别，默认设置为0
     * @param messageCategory 消息分类
     * @param biz             业务系统
     * @return 发送消息是否成功
     */
    boolean sendShortMessageByMobileNumber(String mobileNumber, String message, int timeout, int level, ShortMessageCategory messageCategory, String biz);
}
