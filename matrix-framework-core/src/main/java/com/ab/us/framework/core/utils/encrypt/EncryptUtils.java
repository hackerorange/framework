package com.ab.us.framework.core.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ab.us.framework.core.utils.MD5Util;

import sun.misc.BASE64Encoder;

/**
 * 加密工具类 Created by ZhongChongtao on 2017/3/9.
 */
@SuppressWarnings({ "WeakerAccess", "Duplicates" })
public class EncryptUtils {
	/**
	 * BASE64 加密
	 *
	 * @param value
	 *            待加密字符串
	 * @return 加密后的字符串
	 */
	public static String base64Encrypt(String value) {
		String result = null;
		if (value != null && !"".equals( value.trim() )) {
			result = new BASE64Encoder().encode( value.getBytes() );
		}
		return result;
	}

	/**
	 * MD5 加密
	 *
	 * @param value
	 *            待加密字符
	 * @return 加密后的字符串
	 */
	public static String md5Encrypt(String value) {
		String result = null;
		if (value != null && !"".equals( value.trim() )) {
			result = MD5Utils.encrypt( value, MD5Utils.MD5_KEY );
		}
		return result;
	}

	/**
	 * SHA加密
	 *
	 * @param value
	 *            待加密字符
	 * @return 密文
	 */
	public static String shaEncrypt(String value) {
		String result = null;
		if (value != null && !"".equals( value.trim() )) {
			result = MD5Utils.encrypt( value, MD5Utils.SHA_KEY );
		}
		return result;
	}

	/**
	 * AES加密
	 *
	 * @param message
	 *            待加密内容
	 * @param password
	 *            秘钥
	 * @return 加密后的信息
	 */
	public static String aesEncrypt(String message, String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
			byte[] key = digest.digest( Md5SaltTool.MD5_Encode( password ).getBytes( StandardCharsets.UTF_8 ) );
			SecretKeySpec skeySpec = new SecretKeySpec( key, "AES" );
			final byte[] iv = new byte[16];
			Arrays.fill( iv, (byte) 0x00 );
			IvParameterSpec ivspec = new IvParameterSpec( iv );
			Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
			cipher.init( Cipher.ENCRYPT_MODE, skeySpec, ivspec );
			byte[] encrypted = cipher.doFinal( message.getBytes() );
			return new BASE64Encoder().encode( encrypted );
		} catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| NoSuchPaddingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 掌上安邦后台与前台交互加密
	 *
	 * @param message
	 *            要加密的消息
	 * @param key
	 *            秘钥(没有进行MD5的密钥)
	 * @return 加密后的字符串
	 */
	@SuppressWarnings({ "WeakerAccess", "Duplicates" })
	public static String universeSunAES_Encode(String message, String key) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance( "SHA-256" );
			byte[] keyBytes = messageDigest.digest( Md5SaltTool.MD5_Encode( key ).getBytes( StandardCharsets.UTF_8 ) );
			SecretKeySpec secretKeySpec = new SecretKeySpec( keyBytes, "AES" );
			final byte[] iv = new byte[16];
			Arrays.fill( iv, (byte) 0x00 );
			IvParameterSpec ivParameterSpec = new IvParameterSpec( iv );
			Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
			cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec );
			byte[] encrypted = cipher.doFinal( message.getBytes() );
			return new BASE64Encoder().encode( encrypted );
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
				| InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static void main(String[] args) {
	// CoreAccount coreAccount = new CoreAccount();
	// coreAccount.setAccountId("asjhdfkasd");
	// EncryptUtils.encryptField(coreAccount, "accountId", "aadfsa");
	// System.out.println(coreAccount.getAccountId());
	// }

	/**
	 * 对对象的某个字段加密
	 *
	 * @param object
	 *            要加密的对象
	 * @param fieldName
	 *            加密的字段
	 * @param key
	 *            密钥
	 */
	public static void encryptField(Object object, String fieldName, String key) {
		Class<?> aClass = object.getClass();
		try {
			StringBuffer getter = new StringBuffer( "get" );
			getter.append( fieldName.substring( 0, 1 ).toUpperCase() ).append( fieldName.substring( 1 ) );
			// 反射getter方法
			Method getMethod = aClass.getMethod( String.valueOf( getter ) );
			String originalValue = String.valueOf( getMethod.invoke( object ) );
			String encryptedValue = universeSunAES_Encode( originalValue, key );
			StringBuffer setter = new StringBuffer( "set" );
			setter.append( fieldName.substring( 0, 1 ).toUpperCase() ).append( fieldName.substring( 1 ) );
			// 反射setter方法
			Field field = aClass.getDeclaredField( fieldName );
			Method setMethod = aClass.getMethod( String.valueOf( setter ), field.getType() );
			setMethod.invoke( object, encryptedValue );
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	/**
	 * 加密
	 *
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return 加密后的字符串
	 */
	public static String thirdPlatFormEncorder(String content, String password) {
		try {
			password = MD5Util.getMD5FromString( password );
			// KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			// keyGenerator.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = getKey( password );
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec( enCodeFormat, "AES" );
			Cipher cipher = Cipher.getInstance( "AES" );// 创建密码器
			byte[] byteContent = content.getBytes( "utf-8" );
			cipher.init( Cipher.ENCRYPT_MODE, key );// 初始化
			byte[] result = cipher.doFinal( byteContent );
			return new BASE64Encoder().encodeBuffer( result ); // 加密
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SecretKey getKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance( "AES" );
			SecureRandom secureRandom = SecureRandom.getInstance( "SHA1PRNG" );
			secureRandom.setSeed( strKey.getBytes() );
			_generator.init( 128, secureRandom );
			return _generator.generateKey();
		} catch (Exception e) {
			throw new RuntimeException( " 初始化密钥出现异常 " );
		}
	}

//	public static void main(String[] args) {
//		String code = thirdPlatFormEncorder( "orangeaskdfjlsjdfl", "abc" );
//		System.out.println( code );
//		String s = DecryptUtils.thirdPlatFormDecoder( code, "abc" );
//		System.out.println( s );
//	}
}
