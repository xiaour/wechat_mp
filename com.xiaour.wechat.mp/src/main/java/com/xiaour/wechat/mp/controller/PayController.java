package com.xiaour.wechat.mp.controller;


import com.google.gson.JsonObject;
import com.xiaour.wechat.mp.constants.CacheKeys;
import com.xiaour.wechat.mp.entity.UnifiedOrderDto;
import com.xiaour.wechat.mp.entity.WxOrderQuery;
import com.xiaour.wechat.mp.entity.WxRefundDto;
import com.xiaour.wechat.mp.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付、退款
 *
 * @ClassName PayController
 * @author Zhang.Tao
 * @Date 2017年6月8日 下午4:25:33
 * @version V2.0.0
 */

@RestController
@RequestMapping("/pay")
public class PayController {
	
	  private final Logger logger = LoggerFactory.getLogger(this.getClass());
	  
	  private static final String notifySuccess="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

	  
	  @Value("${server.payNotify}")
	  private String notifyUrl;
	  
	  @Value("${server.payCallbackUrl}")
	  private String payCallbackUrl;


	  @Autowired
	  private WechatUtil wechatUtil;
	  
	  @Autowired
	  private RestTemplate restTemplate;
	  

	
	/**
	 * 
	 * @Description 付款
	 * @return
	 */
	public String pay(){
		return null;
	}
	
	/**
	 * 
	 * @Description 产生预订单，统一下单接口
	 * @param appid 公众号ID
	 * @param payFee 支付金额 （分）
	 * @param openid 用户openid
	 * @param detailJson 订单明细
	 * @param orderId 订单编号
	 * @param attach 描述
	 * @param title 支付页面显示的title
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "unifiedorder", method = { RequestMethod.GET, RequestMethod.POST })
	public String unifiedorder(String appid,final int payFee,String openid,String detailJson,String orderId,String attach,String title){
		UnifiedOrderDto dto= new UnifiedOrderDto();
		try {
			String mchId=wechatUtil.getCacheWxData(appid, CacheKeys.WX_MCHID);
			String redomStr=SignUtils.getRandomString(12);
			String keys=wechatUtil.getCacheWxData(appid,CacheKeys.WX_MCHSIGNKEY);

			dto.setAppid(appid);

			dto.setMch_id(mchId);

			dto.setAttach(attach);

			dto.setBody(title);

			dto.setDetail(detailJson);

			dto.setNonce_str(redomStr);

			dto.setNotify_url(notifyUrl);

			dto.setOut_trade_no(orderId);

			dto.setTotal_fee(String.valueOf(payFee));

			dto.setOpenid(openid);//当trade_type为NATIVE时，此项为空则表示任意帐号可以支付。

			dto.setTrade_type("JSAPI");//JSAPI\NATIVE

			dto.setSpbill_create_ip("127.0.0.1");

			Map<String,String> data= JsonUtil.readJson2MapObj(JsonUtil.getJsonString(dto),String.class);

			dto.setSign(SignUtils.createSign(data,keys));

			logger.info("-------------------->"+dto.getSign()+":::::::"+redomStr);

			String xml=wechatUtil.unifiedorder(dto.toXml());
			logger.info(xml);
			return JsonResult.successWithData(getSignMap(keys, Xml2JsonUtil.xml2Json(xml).toString(),dto));
		} catch (Exception e) {
			logger.error("PAY_ERROR_01",e);
			logger.error("PAY_ERROR_02",dto.toXml());
			return JsonResult.fail();
		}
	}


	/**
	 * 封装打包支付消息提返回给前端
	 * @param signKey
	 * @param resultJson
	 * @param dto
	 * @return
	 */
	private SortedMap<String, String> getSignMap(String signKey,String resultJson,UnifiedOrderDto dto){
		Map<String,Object> data=JsonUtil.readJson2MapObj(resultJson,Object.class);
		 SortedMap<String, String> finalpackage = new TreeMap<>();
         String timestamp =SignUtils.getTimeStamp();
         String packages = "prepay_id=" + data.get("prepay_id");
         finalpackage.put("appId",dto.getAppid());
         finalpackage.put("timeStamp", timestamp);
         finalpackage.put("nonceStr",dto.getNonce_str());
         finalpackage.put("package", packages);
         finalpackage.put("signType", "MD5");
         finalpackage.put("pay_sign", SignUtils.createSign(finalpackage,signKey));
         finalpackage.put("prepay_id", data.get("prepay_id").toString());
         finalpackage.put("order_id",dto.getOut_trade_no());
         return finalpackage;
	}
	
	
	/**
	 * @Description 查询已支付订单
	 * @param appid
	 * @param transactionId 微信订单号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "orderquery", method = { RequestMethod.GET, RequestMethod.POST })
	public String orderquery(String appid,String transactionId){
		try {
			String mchId=wechatUtil.getCacheWxData(appid,CacheKeys.WX_MCHID);
			WxOrderQuery dto= new WxOrderQuery();
			String redomStr=SignUtils.getRandomString(12);
			String keys=wechatUtil.getCacheWxData(appid,CacheKeys.WX_MCHSIGNKEY);

			dto.setAppid(appid);

			dto.setMch_id(mchId);

			dto.setNonce_str(redomStr);

			dto.setTransaction_id(transactionId);

			Map<String,String> data= JsonUtil.readJson2MapObj(JsonUtil.getJsonString(dto),String.class);

			dto.setSign(SignUtils.createSign(data,keys));

			String xml=wechatUtil.queryOrder(dto.toXml());
			
			return JsonResult.successWithData(Xml2JsonUtil.xml2Json(xml));
			
		} catch (Exception e) {
			logger.error("",e);
			return JsonResult.fail();
		}
	}
	
	
	/**
	 * 支付成功后通知处理：因为微信返回的支付信息结果是XML的，这里转义成JSON了，可以在yml{payCallbackUrl}中设置处理付款成功的接口
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "payNotify", method ={ RequestMethod.GET, RequestMethod.POST })
	@PostMapping(produces = "application/xml; charset=UTF-8")
	public String payNotify(@RequestBody String requestBody) {
		
		try {
			JsonObject json=Xml2JsonUtil.xml2Json(requestBody);
			
			if(json.get("return_code").getAsString().equals("SUCCESS")){
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(payCallbackUrl+"?backParams="+json);
				URI uri = builder.build().encode().toUri();
				String result = restTemplate.getForEntity(uri,String.class).getBody();
				logger.info("ORDER SUCCESS:"+result);
				return notifySuccess;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @Description 微信退款
	 * @param totalFee 订单金额（分）
	 * @param refundFee 退款金额（分）
	 * @param refundNo 系统退款单号

	 * @param transactionId 微信订单号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "refund", method = { RequestMethod.GET, RequestMethod.POST })
	public String refund(String appid,String openid,final int totalFee,final int refundFee,String refundNo,String transactionId,@Value("${server.wxPayCertPath}")String keyPath){
		if(StringUtils.isEmpty(openid)){
			return JsonResult.result(500,"openid不能为空");
		}

		/**
		 * TODO:这里可以做用户信息校验，appid也可以从用户信息去对应。
		 */
		Thread thread=new Thread(new Runnable() {
			public void run() {
				try {
					String mchId=wechatUtil.getCacheWxData(appid,CacheKeys.WX_MCHID);
					if(!StringUtils.isEmpty(mchId)){
						WxRefundDto dto= new WxRefundDto();
						String redomStr=SignUtils.getRandomString(12);
						String keys=wechatUtil.getCacheWxData(appid,CacheKeys.WX_MCHSIGNKEY);
						dto.setAppid(appid);
						dto.setMch_id(mchId);
						dto.setNonce_str(redomStr);
						dto.setOut_refund_no(refundNo!=null?refundNo:transactionId);
						dto.setTransaction_id(transactionId);
						dto.setTotal_fee(totalFee);
						dto.setRefund_fee(refundFee);
						Map<String,String> data= JsonUtil.readJson2MapObj(JsonUtil.getJsonString(dto),String.class);
						dto.setSign(SignUtils.createSign(data,keys));
					}else{
						logger.error("退款失败：公众号没有配置支付商户ID，无法进行退款");
					}

				} catch (Exception e) {
					logger.error("",e);
				}
			}});
			thread.start();
			
		return JsonResult.successWithData("正在进行退款，稍后会将退款退回支付账户!");
	}


}
