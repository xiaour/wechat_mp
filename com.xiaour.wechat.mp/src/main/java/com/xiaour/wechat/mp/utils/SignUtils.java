package com.xiaour.wechat.mp.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignUtils {


	  /**
	   * 微信公众号支付签名算法(详见:https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=4_3)
	   *
	   * @param params  参数信息
	   * @param signKey 签名Key
	   * @return 签名字符串
	   */
	  public static String createSign(Map<String, String> params, String signKey) {
		  
	    SortedMap<String, String> sortedMap = new TreeMap<>(params);

	    StringBuilder toSign = new StringBuilder();

	    for (String key : sortedMap.keySet()) {
	    	
	      String value = String.valueOf(params.get(key));

	      if (!StringUtils.isEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
	        toSign.append(key + "=" + value + "&");
	      }
	    }

	    toSign.append("key=" + signKey);

	    return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
	  }

	  
	  public static String getRandomString(int length) { //length表示生成字符串的长度  
		    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
		    Random random = new Random();     
		    StringBuffer sb = new StringBuffer();     
		    for (int i = 0; i < length; i++) {     
		        int number = random.nextInt(base.length());     
		        sb.append(base.charAt(number));     
		    }     
		    return sb.toString();     
		 }    


	  /**
	   * 校验签名是否正确
	   *
	   * @param params  需要校验的参数Map
	   * @param signKey 校验的签名Key
	   * @return true - 签名校验成功，false - 签名校验失败
	   * @see #checkSign(Map, String)
	   */
	  public static boolean checkSign(Map<String, String> params, String signKey) {
	    String sign = createSign(params, signKey);
	    return sign.equals(params.get("sign"));
	  }
	  
	  
	  public static String getTimeStamp() {
	        return String.valueOf(System.currentTimeMillis() / 1000);
	    }
	  
	  public static void main(String[] args) {
		/*Map<String,String> data= new HashMap<>();
		data.put("appid","wxd930ea5d5a258f4f");
		data.put("body","test");
		data.put("device_info","WEB");
		data.put("mch_id","1259556301");
		data.put("nonce_str","123456");*/
	  }
}
