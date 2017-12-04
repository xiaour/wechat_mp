package com.xiaour.wechat.mp.constants;

public class CacheKeys {
	
	public static final String PRODUCT_NAME="WECHAT";
	
	public static final String ACCOUNT="ACCOUNT";//公众号
	
	public static final String ACCESS_TOKEN="ACCESS_TOKEN";//OAUTH2 token

	public static final String LOGIN_USER="LOGIN_USER";//登录用户
	
	public static final String QRCODE_ACTIVITY="QRCODE";//二维码活动
	
	public static final String QRCODE_INTOSTORE="QRCODE:INTOSTORE";//二维码进店



	
	public static final String WX_ACCESSTOKEN="access_token";
	
	public static final String WX_TICKET="ticket";
	
	public static final String WX_SECRET="secret";
	
	public static final String WX_AESKEY="aeskey";
	
	public static final String WX_TOKEN="token";//微信基本认证的TOKEN
	
	

	/**
	 * 商户ID
	 */
	public static final String WX_MCHID="mch_id";
	
	/**
	 * 商户签名密钥
	 */
	public static final String WX_MCHSIGNKEY="mch_sign_key";
	
	
	/**
	 * 
	 * @Description 获取登录用户的key
	 * @param openid
	 * @return
	 */
	public static String getLoginUserKey(String openid){
		return PRODUCT_NAME+":"+LOGIN_USER+":"+openid;
	}
	
	/**
	 * 
	 * @Description 获取缓存的key
	 * @param appId
	 * @return
	 */
	public static String getWxAccountKey(String appId){
		return PRODUCT_NAME+":"+ACCOUNT+":"+appId;
	}
	
	/**
	 * 
	 * @Description 获取token的key
	 * @param appId
	 * @return
	 */
	public static String getTokenKey(String appId){
		return PRODUCT_NAME+":"+ACCESS_TOKEN+":"+appId;
	}

	
	/**
	 * 
	 * @Description 获取缓存的key
	 * @param qrcodeId
	 * @return
	 */
	public static String getQRCcodeActivityKey(String qrcodeId){
		return PRODUCT_NAME+":"+QRCODE_ACTIVITY+":"+qrcodeId;
	}

	


}
