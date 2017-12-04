package com.xiaour.wechat.mp.utils;

public class WxValidateUtil {
	
	
	public static String getTimestampStr(){
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	/**
	 * 获取指定长度的随机字符串
	 * 
	 * @param len
	 * @return
	 */
	public static String getRandomStr(int len) {
		char[] chars = new char[62];
		for (int i = 0; i < 10; i++) {
			chars[i] = (char) ('0' + i);
		}
		for (int i = 0; i < 26; i++) {
			chars[10 + i] = (char) ('a' + i);
			chars[36 + i] = (char) ('A' + i);
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int a = (int) (Math.random() * 62);
			sb.append(chars[a]);
		}
		return sb.toString();
	}

}
