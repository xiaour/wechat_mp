package com.xiaour.wechat.mp.controller;


import com.xiaour.wechat.mp.entity.WxXmlOutMessage;
import com.xiaour.wechat.mp.exception.ApiException;
import com.xiaour.wechat.mp.service.WxAccountService;
import com.xiaour.wechat.mp.utils.JsonResult;
import com.xiaour.wechat.mp.utils.SHA1;
import com.xiaour.wechat.mp.utils.WechatUtil;
import com.xiaour.wechat.mp.utils.WxValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信下次回调接口
 *
 * @ClassName ServerController
 * @author Zhang.Tao
 * @Date 2017年6月8日 下午4:27:01
 * @version V2.0.0
 */

@RestController
@RequestMapping("/server/{appid}")
public class ServerController {
	
	  private final Logger logger = LoggerFactory.getLogger(this.getClass());

	  @Autowired
	  private WxAccountService wxAccountService;
	  
	  @Autowired
	  private WechatUtil wechatUtil;

	
	 @ResponseBody
	  @GetMapping(produces = "text/plain;charset=utf-8")
	  public String authGet(@PathVariable(name="appid") String appid, @RequestParam("signature") String signature,
                            @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce,
                            @RequestParam("echostr") String echostr) {
	    this.logger.info("\n接收到来自微信服务器的认证消息：[{},{},{},{}]", signature, timestamp, nonce, echostr);
		 try {
			 if (wxAccountService.checkSignature(appid,timestamp, nonce, signature)) {
               return echostr;
             }
		 } catch (ApiException e) {
			logger.error("",e);
		 }
		 return "非法请求";
	  }
	 
	  @ResponseBody
	  @PostMapping(produces = "application/xml; charset=UTF-8")
	  public String post(@PathVariable(name="appid") String appid, @RequestBody String requestBody, @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce, @RequestParam("signature") String signature,
                         @RequestParam(name = "encrypt_type", required = false) String encType,
                         @RequestParam(name = "msg_signature", required = false) String msgSignature) {
	    this.logger.info(
	        "\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
	            + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
	        signature, encType, msgSignature, timestamp, nonce, requestBody);

		  String out = null;
		  try {
			  if (!wxAccountService.checkSignature(appid,timestamp, nonce, signature)) {
                  this.logger.info("非法的请求：可能属于伪造请求，已拦截");
              }

			if (encType == null) {
			   // 明文传输的消息
			    WxXmlOutMessage outMessage= wxAccountService.callbackUserMessage(appid, requestBody);
			    if(outMessage!=null){
			    	out=outMessage.toXml();
			    }
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
	    
	    return out;
	  }

	  
		/**
		 * @Description 获取网页授权
		 * @param url
		 * @param state
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "config", method = { RequestMethod.GET, RequestMethod.POST })
		public String config(@PathVariable(name="appid") String appid, @RequestParam(value = "url")String url, @RequestParam(value = "state",required=false)String state){

			try {
				String cacheTicked=wechatUtil.getCacheJsApiTicket(appid,wechatUtil.getAccessToken(appid, null));

				String noncestr= WxValidateUtil.getRandomStr(8);

				String timestamp=WxValidateUtil.getTimestampStr();

				if(state!=null&&state!=""){
					url+="&state=STATE";
				}
				String sha1Str="jsapi_ticket="+cacheTicked+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;

				String signature=new SHA1().getDigestOfString(sha1Str.getBytes());

				Map<String,Object> map= new HashMap<>();

				map.put("appid",appid);

				map.put("signature", signature);

				map.put("noncestr", noncestr);

				map.put("timestamp", timestamp);

				return JsonResult.successWithData(map);

			}catch (Exception e){
				logger.error("",e);
				return JsonResult.success();
			}
		}
		
		/**
		 * @Description 通过code获取用户信息
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "getUserInfoByCode", method = { RequestMethod.GET, RequestMethod.POST })
		public String getUserInfoByCode(@PathVariable(name="appid") String appid, @RequestParam(value = "code")String code){
			Map<String,Object> map= new HashMap<>();
			try {
				Map<String, Object> jsonMap=wechatUtil.getOAuthInfo(code,appid);
				if(jsonMap!=null){

					map.put("dataInfo",jsonMap);
					return JsonResult.successWithData(map);

				}
			} catch (Exception e) {
				logger.error("",e);
			}
			return JsonResult.fail();
		}

	/**
	 * @Description 通过code获取用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOpenidByCode", method = { RequestMethod.GET, RequestMethod.POST })
	public String getOpenidByCode(@PathVariable(name="appid") String appid, @RequestParam(value = "code")String code){
		Map<String,Object> map= new HashMap<>();
		try {
			Map<String, Object> jsonMap=wechatUtil.getOAuthInfo(code,appid);
			if(jsonMap!=null){
				map.put("openid",jsonMap.get("openid"));
				return JsonResult.successWithData(map);
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return JsonResult.fail();
	}



}
