package com.xiaour.wechat.mp.controller;


import com.xiaour.wechat.mp.constants.CacheKeys;
import com.xiaour.wechat.mp.entity.WxAccount;
import com.xiaour.wechat.mp.service.RedisService;
import com.xiaour.wechat.mp.utils.JsonResult;
import com.xiaour.wechat.mp.utils.JsonUtil;
import com.xiaour.wechat.mp.utils.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 微信公众号维护API接口
 */

@RestController
@RequestMapping("/wechat")
public class WechatController {
	
	  private final Logger logger = LoggerFactory.getLogger(this.getClass());

	  @Autowired
	  private WechatUtil wechatUtil;
	  
	  @Autowired
	  private RedisService  redisService;
	  
	  @Value("${server.QRCodePath}")
	  private String qrcodePath;
	  
	  @Value("${server.wxConfigStoragePath}")
	  private String wxConfigStoragePath;

	  
	  private static Set<String> fileType=new HashSet<>();
	  
	  public WechatController() {
		super();
		fileType.add("p12");
		fileType.add("yml");
	}


	  
	  /**
	   * 
	   * @Description 添加公众号
	   * @param appid
	   * @param secret
	   * @param token
	   * @param aeskey
	   * @return
	   */
	  @RequestMapping(value="/add", method = { RequestMethod.GET, RequestMethod.POST })
	  public String addAccount(@RequestParam("appid") String appid,
			  @RequestParam("secret") String secret,
			  @RequestParam("token") String token,
			  @RequestParam("aeskey") String aeskey, String mchId, String mchSignKey){
		  
		  try {
			  
			WxAccount data=redisService.hget(CacheKeys.getWxAccountKey(appid),WxAccount.class);
			
			  if(data.getId()!=null){
				  return JsonResult.result(500,data.getId()+"公众号已经存在！");
			  }
			  WxAccount account= new WxAccount();
			  account.setId(appid);
			  account.setAeskey(aeskey);
			  account.setSecret(secret);
			  account.setToken(token);
			  account.setMchId(mchId);
			  account.setMchSignKey(mchSignKey);
			  redisService.hmset(CacheKeys.getWxAccountKey(appid),account);
			  //initCache.singleRrefresh(account);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return JsonResult.success();
	  }
	  
	  
	  /**
	   * 
	   * @Description 查询公众号
	   * @param appid
	   * @return
	   */
	  @RequestMapping(value="/getAccount", method = { RequestMethod.GET, RequestMethod.POST })
	  public String getAccount(@RequestParam("appid") String appid){
		  
		  try {

			  WxAccount data=redisService.hget(CacheKeys.getWxAccountKey(appid),WxAccount.class);
			return JsonResult.successWithData(data);
			} catch (Exception e) {
				logger.error("",e);
				return JsonResult.fail();
			}
	  }
	  

	  
	  /**
	   * @Description 查询公众号列表
	   * @return
	   */
	  @RequestMapping(value="/list")
	  public String getList(){
		  try {

			  List<WxAccount> list=redisService.hmGetAll(CacheKeys.getWxAccountKey("*"),WxAccount.class);
			  return JsonResult.successWithData(list);
		} catch (Exception e) {
			logger.error("",e);
			return JsonResult.fail();
		}
	  }
	  
	  /**
		 * @Description 
		 * @param url
		 * @return
		 */
		/*@ResponseBody
		@RequestMapping(value = "sendTemplateMsg", method = { RequestMethod.GET, RequestMethod.POST })
		public String sendTemplateMsg(@RequestParam(value="appid") String appid,
				@RequestParam(value="touser") String touser,
				@RequestParam(value="msgType") Integer msgType,
				@RequestParam(value="url",required=false)String url,
				@RequestParam(value="msgJson") String msgJson){
			//msgJson="{\"key-1\":\"北京市海淀区中关村银科大厦712号\",\"key-n\":\"...\",\"key-2\":\"用餐份数\",\"remark\":\"详情请点击链接查看哦！\",\"first\":\"尊敬的用户\"}"; 
			try {

				Map<String, Object> teplateMap=MsgTemplateWatch.getTemplateMap(appid, msgType);
				
				String templateJson= JsonUtil.getJsonString(teplateMap);
				
				templateJson=templateJson.replaceAll("\\{\\{touser\\}\\}", touser).replaceAll("\\{\\{url\\}\\}",url);
				
				
				Map<String,String> msgData=JsonUtil.readJson2Obj(msgJson,Map.class);
				
				if(!msgData.containsKey("first")||!msgData.containsKey("remark")){
					JsonResult.failureWithTips("消息内容参数不正确，请检查是否包含first&remark！");
				}
				
				for (Entry<String, String> entry : msgData.entrySet()) {  
					templateJson=templateJson.replaceAll("\\{\\{"+String.valueOf(entry.getKey())+"\\}\\}",String.valueOf(entry.getValue()));
				} 
				
				String msgId=wechatUtil.sendTemplateMsg(appid,JsonUtil.readJson2Obj(templateJson,Map.class));
				
				if(!StringUtils.isEmpty(msgId)){
					
					return JsonResult.emptySuccess();
					
				}else{
					
					return JsonResult.emptyFailure();
				}
			
			} catch (OApiException e) {
				logger.error("",e);
				return JsonResult.emptyFailure();
			}
		}*/

		  

		  
		  /**
		   * @Description 发送客服消息
		   * @param appid
		   * @param openid
		   * @param content
		   * @return
		   */
		  @ResponseBody
		  @RequestMapping(value = "sendKefuMsg", method = { RequestMethod.GET, RequestMethod.POST })
		  public String sendKefuMsg(String appid,String type,String openid,String content) {
			  Thread t= new Thread(new Runnable() {
				@Override
				public void run() {
					 Map<String,Object> data= new HashMap<>();
					  data.put("touser",openid);
					  data.put("msgtype",type);
					  data.put(type, JsonUtil.readJsonStrMap(content));
					try {
						wechatUtil.sendCustomMsg(appid, data);
					} catch (Exception e) {
						logger.error("",e);
					}
				}
			});
			  t.start();
		    return JsonResult.success();
		  }
		  
		  @ResponseBody
		  @RequestMapping(value = "sysdate", method = { RequestMethod.GET, RequestMethod.POST })
		  public String sysdate() {
		    return JsonResult.successWithData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		  }
		  
		  
		  @RequestMapping(value="/upload")
		  public  String uploadCert(@RequestParam(value = "fileInfo") MultipartFile file,String appid) throws IOException {
			  	if(file==null){
					return JsonResult.result(500,"没有任何文件");
			  	}
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);

				
				if(!fileType.contains(suffix)){
					return  JsonResult.result(500,"证书格式不正确，请上传p12格式的证书！");
				}
				String path="";
				switch (suffix) {
				case "p12":
					path=wxConfigStoragePath;
					break;
				case "yml":
					path=wxConfigStoragePath;
					break;
				default:
					break;
				}
				
				String fileName = appid+"."+suffix;
		        
		        File folder = new File(path);
		        
		        if(!folder.exists()){
		        	folder.mkdirs();
		        }
		        FileOutputStream fs = new FileOutputStream(path+fileName);
		        InputStream stream =file.getInputStream();
				try {
					byte[] buffer =new byte[1024*1024];
					int byteread = 0;
					while ((byteread=stream.read(buffer))!=-1)
					{   
					   fs.write(buffer,0,byteread);   
					   fs.flush();   
					}
				} catch (Exception e) {
					logger.error("文件上传异常：",e);
				} finally{   
			        stream.close(); 
			        fs.close();
				}
		        return JsonResult.success();
		    }

}
