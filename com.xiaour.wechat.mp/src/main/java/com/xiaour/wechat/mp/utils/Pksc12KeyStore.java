package com.xiaour.wechat.mp.utils;

import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;


public class Pksc12KeyStore {


	  public static SSLContext initSSLContext(String appid,String mchId,String keyPath) {
	    if (null == mchId) {
	      throw new IllegalArgumentException("mch_id不能为空");
	    }

	    File file = new File(keyPath+appid+".p12");
	    if (!file.exists()) {
	      throw new RuntimeException("初始化证书异常" + file.getPath() + "没有找到证书文件");
	    }

	    try {
	      FileInputStream inputStream = new FileInputStream(file);
	      KeyStore keystore = KeyStore.getInstance("PKCS12");
	      char[] partnerId2charArray = mchId.toCharArray();
	      keystore.load(inputStream, partnerId2charArray);
	      return SSLContexts.custom().loadKeyMaterial(keystore, partnerId2charArray).build();
	    } catch (Exception e) {
	      throw new RuntimeException("初始化证书异常", e);
	    }
	  }
}
