/*
package com.xiaour.wechat.mp.task;


import com.fantong.wechat.wes.service.AccountService;
import com.fantong.wechat.wes.util.WechatUtil;
import com.xiaour.wechat.mp.constants.CacheKeys;
import com.xiaour.wechat.mp.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Component
public class InitCache implements CommandLineRunner {


	 
	 @Autowired
	 private AccountService accountService;
	 
	 @Autowired
	 private WechatUtil wechatUtil;

	*/
/**
	 * 初始化公众号ACCESSTOKEN
	 *//*

	//
	@Override
	public void run(String... arg0){
		try {
			refresh();
		} catch (Exception e) {
			log.error("",e);
		}

	}
	
	*/
/**
	 * @Description 更新全部的认证信息
	 *//*

	@Scheduled(cron="0 0 0/1 * * ?")
	public void refresh() throws Exception{

		Account account= new Account();
		account.setState(1);
		List<Account> list=accountService.selectByCondition(account);
		//Jedis jedis=null ;
		try {
			//jedis = jedisPool.getResource();
			Map<String,String> map;

			for(Account acct:list){
				map= new HashMap<>();
				String token=wechatUtil.getAccessToken(acct.getId(),acct.getSecret());
				String ticket=wechatUtil.getJsApiTicket(token);
				if(token!=null&&ticket!=null){
					map.put(CacheKeys.WX_ACCESSTOKEN, token);
					map.put(CacheKeys.WX_TICKET, ticket);
					map.put(CacheKeys.WX_SECRET, acct.getSecret());
					map.put(CacheKeys.WX_AESKEY, acct.getAeskey());
					map.put(CacheKeys.WX_TOKEN, acct.getToken());

					if(acct.getMchId()!=null){
						map.put(CacheKeys.WX_MCHID, acct.getMchId());
					}
					if(acct.getMchSignKey()!=null){
						map.put(CacheKeys.WX_MCHSIGNKEY, acct.getMchSignKey());
					}
					*/
/*jedis.hmset(CacheKeys.getTokenKey(acct.getId()),map);
					jedis.expire(CacheKeys.getTokenKey(acct.getId()),7200);*//*

				}

			}
		//存储品牌及门店
		}catch(Exception e){
			throw new ApiException(e);
		}
	}
	
	*/
/**
	 * 
	 * @Description 更新单个账号的认证信息
	 * @param acct
	 *//*

	public void singleRrefresh(Account acct) throws  Exception{

		try {

		Map<String,String> map;
			map= new HashMap<>();
			String token=wechatUtil.getAccessToken(acct.getId(),acct.getSecret());
			String ticket=wechatUtil.getJsApiTicket(token);
			if(token!=null&&ticket!=null){
				map.put(CacheKeys.WX_ACCESSTOKEN, token);
				map.put(CacheKeys.WX_TICKET, ticket);
				map.put(CacheKeys.WX_SECRET, acct.getSecret());
				map.put(CacheKeys.WX_AESKEY, acct.getAeskey());
				map.put(CacheKeys.WX_MCHID, acct.getMchId());
				*/
/*jedis.hmset(CacheKeys.getTokenKey(acct.getId()),map);
				jedis.expire(CacheKeys.getTokenKey(acct.getId()),7200);*//*

			}
				
		//存储品牌及门店
		}catch(Exception e){
			throw new ApiException(e);
		}
	}
	
	public static void main(String[] args) {
		
	}

}
*/
