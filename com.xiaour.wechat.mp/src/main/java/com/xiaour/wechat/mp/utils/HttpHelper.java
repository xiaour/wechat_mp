package com.xiaour.wechat.mp.utils;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaour.wechat.mp.entity.WxRefundDto;
import com.xiaour.wechat.mp.exception.ApiException;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.net.SSLSocketHttpConnectionProvider;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;


public class HttpHelper {
	

	public static JsonObject httpGet(String url) throws ApiException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().
        		setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);

        try {
            response = httpClient.execute(httpGet, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                return  new JsonParser().parse(resultStr).getAsJsonObject();

            }
        } catch (IOException e) {
        	throw new ApiException(e);
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
            	throw new ApiException(e);
            }
        }

        return null;
    }
	
	
	public static String postXmlStr(String url,String xmlStr) throws ApiException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().
        		setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/xml");
        try {
        	StringEntity requestEntity = new StringEntity(xmlStr, "utf-8");
            httpPost.setEntity(requestEntity);
            
            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                return resultStr;
            }
        } catch (IOException e) {
        	throw new ApiException(e);
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
            	throw new ApiException(e);
            }
        }

        return null;
    }
	
	
	public static JsonObject httpPost(String url, Object data) throws ApiException {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/json");
        try {

        	String dataStr=new Gson().toJson(data);

        	StringEntity requestEntity = new StringEntity(dataStr, "utf-8");

            httpPost.setEntity(requestEntity);
            
            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                return null;
            }
            
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                return new JsonParser().parse(resultStr).getAsJsonObject();
            }
        } catch (IOException e) {
        	throw new ApiException(e);
        } finally {
            if (response != null) try {
                response.close();
            } catch (IOException e) {
            	throw new ApiException(e);
            }
        }

        return null;
    }
	
	
    public static String postWithKey(String appid,String keyPath, String url, WxRefundDto dto) throws Exception {
	      SSLContext sslContext = Pksc12KeyStore.initSSLContext(appid,dto.getMch_id(),keyPath);
	      HttpRequest request = HttpRequest.post(url).withConnectionProvider(new SSLSocketHttpConnectionProvider(sslContext));
	      request.bodyText(dto.toXml());
	      HttpResponse response = request.send();
	      return  response.bodyText();
	}
	  
	
}
