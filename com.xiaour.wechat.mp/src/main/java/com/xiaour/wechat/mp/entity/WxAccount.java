package com.xiaour.wechat.mp.entity;

import java.util.Date;

public class WxAccount {
    private String secret;

    private String token;

    private String aeskey;

    private Date createTime;

    private Date updateTime;

    private Integer state;
    
    private String id;//公众号APPID
    
    private String mchId;//微信支付商户号
    
    private String mchSignKey;//支付商户签名key
    
    

    public String getMchSignKey() {
		return mchSignKey;
	}

	public void setMchSignKey(String mchSignKey) {
		this.mchSignKey = mchSignKey;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey == null ? null : aeskey.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}