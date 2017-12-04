package com.xiaour.wechat.mp.service.impl;

import com.xiaour.wechat.mp.constants.CacheKeys;
import com.xiaour.wechat.mp.entity.WxTextMessage;
import com.xiaour.wechat.mp.entity.WxXmlOutMessage;
import com.xiaour.wechat.mp.exception.ApiException;
import com.xiaour.wechat.mp.service.WxAccountService;
import com.xiaour.wechat.mp.utils.SHA1;
import com.xiaour.wechat.mp.utils.WechatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xiaour.zhang on 2017/11/24.
 */

@Service("wxAccountService")
@Transactional(rollbackFor = Exception.class)
public class WxAccountServiceImpl implements WxAccountService {

    @Autowired
    private WechatUtil wechatUtil;

    @Override
    public boolean checkSignature(String appid, String timestamp, String nonce, String signature) throws ApiException {
        String accessToken=wechatUtil.getCacheAccessToken(appid);
        if(accessToken!=null){
            return SHA1.gen(wechatUtil.getCacheWxData(appid, CacheKeys.WX_TOKEN),timestamp, nonce).equals(signature);
        }else{
            return false;
        }
    }

    @Override
    public WxXmlOutMessage callbackUserMessage(String appid, String requestXml) throws ApiException {
        WxXmlOutMessage outMessage = new WxXmlOutMessage();
        //解析微信发过来的消息
        WxTextMessage inMessage = new WxTextMessage().getMsgEntity(requestXml);

        switch (inMessage.getMsgType().toString()) {
            case "text":
                //根据自己需要做处理就好了，还有很多消息类型，参照https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140453
                break;
            case "event":
                //根据自己需要做处理就好了
                    break;
            default:
                break;
        }
        return outMessage;
    }
}
