package com.xiaour.wechat.mp.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaour.wechat.mp.constants.CacheKeys;
import com.xiaour.wechat.mp.entity.WxRefundDto;
import com.xiaour.wechat.mp.exception.ApiException;
import com.xiaour.wechat.mp.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WechatUtil {
	
	  @Value("${server.QRCodePath}")
	  private String qrcodePath;

	  @Autowired
	  private RedisService redisService;
	
	/**
	 * 
	 * @Description 从缓存获取微信Access_token
	 * @param appId
	 * @return
	 */
	public  String getCacheAccessToken(String appId) throws ApiException{


		Map<String,String> map=redisService.hget(CacheKeys.getTokenKey(appId),Map.class);
		if(map!=null){
			return map.get(CacheKeys.WX_ACCESSTOKEN);
		}else{
			return getAccessToken(appId,getCacheSecret(appId));
		}

	}
	
	
	/**
	 * 从缓存获取微信Access_token
	 * @return
	 */
	public  String getCacheSecret(String appId) throws ApiException{

		Map<String,String> map=redisService.hget(CacheKeys.getTokenKey(appId),Map.class);
		if(map!=null){
			return map.get(CacheKeys.WX_SECRET);
		}

		return null;
	}
	
	/**
	 * 从缓存获取微信信息
	 * @return
	 */
	public  String getCacheWxData(String appId,String keys) throws ApiException{

		Map<String,String> map=redisService.hget(CacheKeys.getTokenKey(appId),Map.class);
		if(map!=null){
			return map.get(keys);
		}

		return null;
	}
	
	
	/**
	 * 
	 * @Description 从微信服务器获取最新AccessToken
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	public  String getAccessToken(String appId,String appSecret) throws ApiException{

		StringBuilder url = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");
		url.append("&appid=" + appId);
		url.append("&secret=" + appSecret);
		JsonObject jsonObj = HttpHelper.httpGet(url.toString());
		if (jsonObj != null&&jsonObj.has(CacheKeys.WX_ACCESSTOKEN)) {

			return jsonObj.get(CacheKeys.WX_ACCESSTOKEN).getAsString();
		}

		return null;
	}
	
	/**
	 * 获取微信Js接口的ticket
	 * @return
	 */
	public  String getJsApiTicket(String accessToken) throws ApiException{

		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";

		JsonObject jsonObj = HttpHelper.httpGet(url.toString());
		if (jsonObj.has(CacheKeys.WX_TICKET)) {
			return jsonObj.get(CacheKeys.WX_TICKET).getAsString();
		}

		return "";
	} 
	
	/**
	 * 获取微信Js接口的ticket
	 * @return
	 */
	public  String getCacheJsApiTicket(String appid,String accessToken) throws ApiException{

		Map<String,String> map=redisService.hget(CacheKeys.getTokenKey(appid),Map.class);
		if(map!=null){
			return map.get(CacheKeys.WX_TICKET);
		}else{
			return getJsApiTicket(accessToken);
		}
	} 
	
	
	
	/**
	 * 获取临时带参数的二维码的ticket
	 * 
	 * @param sceneId
	 * @return
	 */
	public  String getTempQrTecket(String appid,Integer sceneId, int expireSeconds) throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
				+ getCacheAccessToken(appid);
		String showUrl="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		expireSeconds = expireSeconds > 2592000 ? 2592000 : expireSeconds;
		
		Map<String,Object> thirdChild=new HashMap<>();
		thirdChild.put("scene_id",sceneId);
		Map<String,Object> child= new HashMap<>();
		child.put("scene",thirdChild);		
		Map<String,Object> map= new HashMap<>();
		map.put("expire_seconds", expireSeconds);
		map.put("action_name","QR_SCENE");
		map.put("action_info",child);

		JsonObject jsonObj = HttpHelper.httpPost(url,map);
		if (jsonObj.has(CacheKeys.WX_TICKET)) {
			return FileUtils.downLoadFromUrl(showUrl+ jsonObj.get(CacheKeys.WX_TICKET),SignUtils.getRandomString(8)+".jpg",qrcodePath);
		}
		return null;
	}
	
	/**
	 * 获取永久二维码
	 * @param appid
	 * @return
	 */
	public  String getPermanentQrTecket(String appid,String sceneStr) throws Exception{
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
				+ getCacheAccessToken(appid);
		String showUrl="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		
		Map<String,Object> thirdChild=new HashMap<>();
		thirdChild.put("scene_str",sceneStr);
		Map<String,Object> child= new HashMap<>();
		child.put("scene",thirdChild);		
		Map<String,Object> map= new HashMap<>();
		map.put("action_name","QR_LIMIT_STR_SCENE");
		map.put("action_info",child);

		JsonObject jsonObj = HttpHelper.httpPost(url,map);
		if (jsonObj.has(CacheKeys.WX_TICKET)) {
			return FileUtils.downLoadFromUrl(showUrl+ jsonObj.get(CacheKeys.WX_TICKET).getAsString(),sceneStr+".jpg",qrcodePath);
		}

		return null;
	}
	
	/**
	 * @Description 拉取微信关注者账号列表
	 * @return
	 */
	public List<String>  getWxOpenIdList(String appid)throws ApiException{
		List<String> list= new ArrayList<>();
		
		String nextId="";
		
		int total=1;
		
		boolean flag=true;

		do{
			JsonObject jsonObj =getOpenIdList(appid,nextId);
			if(jsonObj.has("total")){
				flag=true;

				nextId=jsonObj.get("next_openid").getAsString();

				total=jsonObj.get("total").getAsInt();

				JsonArray data=jsonObj.getAsJsonObject("data").getAsJsonArray("openid");

				for(Object id:data){
					list.add(id.toString());
				}

			}else{
				flag=false;
			}
		}while(list.size()<total&&flag);

		return  list;
	}
	
	private JsonObject getOpenIdList(String appid,String nextId) throws ApiException{
		String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+getCacheAccessToken(appid)+"&next_openid="+nextId;
		JsonObject jsonObj = HttpHelper.httpGet(url);
		return jsonObj;
	}
	
	/**
	 * OAuth授权时，用code换取access_token和openid
	 * @param code
	 * @return
	 */
	public  Map<String,Object> getOAuthInfo(String code,String appId) throws ApiException {
		
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
		String getUserInfoUrl="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
		url = String.format(url, appId,getCacheSecret(appId), code);

		JsonObject jsonObj = HttpHelper.httpGet(url);
		if (jsonObj.has("errcode")) {
			return null;
		}else{
			Map<String,Object> data = new HashMap<>();
			data.put("openid", jsonObj.get("openid").getAsString());
			getUserInfoUrl=String.format(getUserInfoUrl,jsonObj.get("access_token").getAsString(),jsonObj.get("openid").getAsString());
			JsonObject userJson=HttpHelper.httpGet(getUserInfoUrl);
			if (userJson.has("errcode")) {
				return null;
			}else{

			  Iterator<String> it = userJson.keySet().iterator();
			   // 遍历jsonObject数据，添加到Map对象
			   while (it.hasNext()){
				   String key = it.next();
				   Object value =userJson.get(key);
				   data.put(key, value);
			   }
			return data;
			}
		}

	}
	
	/**
	 * 
	 * @Description 获取所有消息模板
	 * @param appid
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getAllNoticeTemplate(String appid) throws Exception{
		
		Map<String,String> map= new HashMap<>();
		
		String url="https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="+getCacheAccessToken(appid);
		
		JsonObject jsonObj = HttpHelper.httpGet(url);
		
		if (jsonObj.has("template_list")) {
			
			JsonArray dataList=jsonObj.get("template_list").getAsJsonArray();
			
			System.out.println(dataList);
			
			for(Object data:dataList){
				JsonObject jsonData=new JsonParser().parse(data.toString()).getAsJsonObject();
				map.put(jsonData.get("template_id").getAsString(), getKeys(jsonData.get("content").getAsString()));
			}
			return map;
		}else{
			return null;
		}
	}
	
	private String getKeys(String content){
		StringBuilder keys=new StringBuilder();
		try {
			String key="";
			for(String arg: content.split("\n")){
				if(arg.indexOf(".DATA}}")>0){
					key=arg.substring(arg.indexOf("{{"),arg.indexOf(".DATA")).replace("{{","").trim();
					keys.append(key).append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keys.toString();
	}
	
	/**
	 * @Description 发送模板消息
	 * @param map
	 * @return
	 * @throws ApiException
	 */
	  public String sendTemplateMsg(String appid,Map map) throws ApiException {
	    String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+getCacheAccessToken(appid);
	    final JsonObject jsonObj = HttpHelper.httpPost(url,map);
	    if (jsonObj.get("errcode").getAsInt() == 0) {
	      return jsonObj.get("msgid").getAsString();
	    }
		return null;
	  }
	  
	  
	/**
	 * @Description 统一下单
	 * @param xmlStr
	 * @return
	 * @throws ApiException
	 */
	  public String unifiedorder(String xmlStr) throws ApiException {
	    String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	    final String resultXml = HttpHelper.postXmlStr(url, xmlStr);
		return resultXml;
	  }
	  
	  /**
		 * @Description 查询订单
		 * @param xmlStr
		 * @return
		 * @throws ApiException
		 */
		  public String queryOrder(String xmlStr) throws ApiException {
		    String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		    final String resultXml = HttpHelper.postXmlStr(url, xmlStr);
			return resultXml;
		  }
	  
	  /**
	   * @Description 退款
	   * @param appid
	   * @param keyPath
	   * @param dto
	   * @return
	   * @throws ApiException
	   */
	  public String refund(String appid,String keyPath, WxRefundDto dto) throws Exception {
		    String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		    final String resultXml = HttpHelper.postWithKey(appid,keyPath,url, dto);
			return resultXml;
	  }

	  
	  public JsonObject sendCustomMsg(String appid,Map<String,Object> msg) throws Exception{
		    String token=getCacheAccessToken(appid);
			String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+token;
		  	JsonObject jsonObj = HttpHelper.httpPost(url,msg);
			return jsonObj;
		}


}
