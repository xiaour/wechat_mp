package com.xiaour.wechat.mp.service;

import com.xiaour.wechat.mp.entity.WxXmlOutMessage;
import com.xiaour.wechat.mp.exception.ApiException;

/**
 * Created by xiaour.zhang on 2017/11/24.
 *
 * 多微信公众号维护
 * Tips:多个微信公众号管理可以使用数据库的方式，本接口为了方便，全部都使用Redis。
 */
public interface WxAccountService {

    /**
     * 网页授权加密
     * @param appid
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    boolean checkSignature(String appid,String timestamp, String nonce, String signature) throws ApiException;

    /**
     *
     * @Description 处理微信关注用户发来的消息:这个根据自己需要来决定用户出发的事情要做什么就可以了
     * @param appid
     * @param requestXml
     * @return
     * @throws Exception
     */
    WxXmlOutMessage callbackUserMessage(String appid, String requestXml)throws ApiException;
}
