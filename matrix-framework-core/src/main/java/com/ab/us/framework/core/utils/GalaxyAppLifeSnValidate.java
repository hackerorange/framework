package com.ab.us.framework.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("Duplicates")
public class GalaxyAppLifeSnValidate {
    private final static String SALT = "[HCPGadn\\\\[]Zno]";
    @SuppressWarnings("FieldCanBeLocal")
    private final static String PLAT_FORM_ID = "Galaxy_LifeApp";
    private final static Logger LOGGER = LoggerFactory.getLogger(GalaxyAppLifeSnValidate.class);

    private String sn;
    private String hashcode;
    private String method;

    public GalaxyAppLifeSnValidate(String platformId, String method) {
        this.method = method;
        sn = UUID.randomUUID().toString();
        hashcode = md5Encrypt(sn + platformId + method + SALT);
    }

    /**
     * 根据方法名，获取SN参数，平台ID 为  PLAT_FORM_ID=Galaxy_LifeApp
     *
     * @param method action方法
     */
    public GalaxyAppLifeSnValidate(String method) {
        this(PLAT_FORM_ID,method);
    }

    /**
     * sn校验接口
     *
     * @param sn       sn校验随机数
     * @param method   请求的方法名
     * @param hashCode 加密后的hashCode
     * @return 校验成功，返回true，否则返回false
     */
    public static boolean snValidate(String sn, String method, String hashCode) {
        String sb = "" + sn + PLAT_FORM_ID + method + SALT;
        String generatedHashCode = md5Encrypt(sb);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("[ %-20s ] : %s", "the sn request is", hashCode));
            LOGGER.debug(String.format("[ %-20s ] : %s", "the sn calculate is", generatedHashCode));
        }
        return StringUtil.equals(generatedHashCode, hashCode);
    }

    private static String md5Encrypt(String source) {
        String re_md5 = "HCPGadn\\\\[]Zno";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            //noinspection ForLoopReplaceableByForEach
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
