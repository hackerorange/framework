package com.ab.us.framework.service.push.domain;

import com.ab.us.framework.service.push.constant.CertType;
import com.ab.us.framework.service.push.constant.DevicePlatform;

/**
 * Created by ZhongChongtao on 2017/5/8.
 * 要推送的对象基本信息
 *
 * @since 2.11.0
 */
public abstract class PushTarget implements MessageTarget {

    /**
     * 根据推送设备ID,生成推送目标信息
     *
     * @param platform 推送平台
     * @param tokenId  用户ID
     * @return 推送用户信息
     */
    public static PushTarget getDevoceTarget(DevicePlatform platform, String tokenId) {
        Device pushDeviceInfo = new Device();
        pushDeviceInfo.setPlatform(platform.getCode());
        pushDeviceInfo.setToken(tokenId);
        return pushDeviceInfo;
    }

    /**
     * 根据AccountId,生成推送目标信息
     *
     * @param accountId 用户ID
     * @return 推送用户信息
     */
    public static PushTarget getAccountTarget(String accountId) {
        Account tokenIdPushUserInfo = new Account();
        tokenIdPushUserInfo.setUid(accountId);
        return tokenIdPushUserInfo;
    }

    /**
     * 根据用户四元信息，生成目标信息
     *
     * @param userName 用户名称
     * @param mobile   用户手机号
     * @param certType 用户证件类型
     * @param certCode 用户证件号
     * @return 推送用户信息
     */
    public static PushTarget getUserTarget(String userName, String mobile, CertType certType, String certCode) {
        User fullPushUserInfo = new User();
        fullPushUserInfo.setName(userName);
        fullPushUserInfo.setCert_code(certCode);
        fullPushUserInfo.setCert_type(certType.getCode());
        fullPushUserInfo.setMobile(mobile);
        return fullPushUserInfo;
    }

    /**
     * 用户设备信息
     */
    public static class Device extends PushTarget {
        private String token;
        private String platform;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }

    /**
     * 用户 uid
     */
    public static class Account extends PushTarget {
        private String uid;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    /**
     * 用户四元信息
     */
    public static class User extends PushTarget {
        private String name;
        private int cert_type;
        private String cert_code;
        private String mobile;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCert_type() {
            return cert_type;
        }

        public void setCert_type(int cert_type) {
            this.cert_type = cert_type;
        }

        public String getCert_code() {
            return cert_code;
        }

        public void setCert_code(String cert_code) {
            this.cert_code = cert_code;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }


}
