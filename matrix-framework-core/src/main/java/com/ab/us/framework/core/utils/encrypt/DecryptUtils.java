package com.ab.us.framework.core.utils.encrypt;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ab.us.framework.core.utils.HexUtils;
import com.ab.us.framework.core.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import sun.misc.BASE64Decoder;

/**
 * 解密工具类 Created by ZhongChongtao on 2017/3/9.
 */
//@SuppressWarnings("unused")
public class DecryptUtils {

    private static final Logger logger = LoggerFactory.getLogger(DecryptUtils.class);

    /**
     * BASE64 解密
     *
     * @param value 待解密字符串
     * @return 解密成功返回原码，否则返回null
     */
    public static String base64Decrypt(String value) {

        String result = null;
        try {
            if (value != null && !"".equals(value.trim())) {
                byte[] bytes = new BASE64Decoder().decodeBuffer(value);// .thirdPlatFormDecoder(value);
                result = new String(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 掌上安邦后台与前台交互解密
     *
     * @param code 密文
     * @param key  密钥
     * @return 解密后的消息
     */
    @SuppressWarnings({"WeakerAccess", "Duplicates"})
    public static String universeSunAES_Decode(String code, String key) {
        try {
            byte[] encryptedBytes = new BASE64Decoder().decodeBuffer(code);
            String md5_encode = Md5SaltTool.MD5_Encode(key);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] digest1 = digest.digest(md5_encode.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(digest1, "AES");
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密由统一服务平台获取的信息
     *
     * @param code     要解密的信息
     * @param password 密钥
     * @return 解密后的信息
     */
    public static String platForm_Decode(String code, String password) {
        try {
            int keyLength = 128;
            byte[] keyBytes = new byte[keyLength / 8];
            Arrays.fill(keyBytes, (byte) 0x0);
            byte[] passwordBytes = password.getBytes("UTF-8");
            int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
            System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            Arrays.fill(iv, (byte) 0x00);

            instance.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            //noinspection ConstantConditions
            byte[] bytes = instance.doFinal(HexUtils.toBinary(StringUtils.trimToEmpty(code).toUpperCase()));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对对象的某个字段加密
     *
     * @param object    要加密的对象
     * @param fieldName 加密的字段
     * @param key       密钥
     */
    public static void decryptField(Object object, String fieldName, String key) {
        Class<?> aClass = object.getClass();
        try {
            StringBuffer getter = new StringBuffer("get");
            getter.append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
            // 反射getter方法
            Method getMethod = aClass.getMethod(String.valueOf(getter));
            String originalValue = String.valueOf(getMethod.invoke(object));
            String decryptedValue = universeSunAES_Decode(originalValue, key);
            StringBuffer setter = new StringBuffer("set");
            setter.append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
            // 反射setter方法
            Field field = aClass.getDeclaredField(fieldName);
            Method setMethod = aClass.getMethod(String.valueOf(setter), field.getType());
            setMethod.invoke(object, decryptedValue);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * AES解密
     *
     * @param encryptedCode 待解密内容
     * @param password      解密密钥
     * @return 解密后的数据
     */
    public static String thirdPlatFormDecoder(String encryptedCode, String password) {
        try {
            password = MD5Util.getMD5FromString(password);
            byte[] content = new BASE64Decoder().decodeBuffer(encryptedCode);
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return new String(result, "UTF-8"); //生成字符串
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private static SecretKey getKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

}
