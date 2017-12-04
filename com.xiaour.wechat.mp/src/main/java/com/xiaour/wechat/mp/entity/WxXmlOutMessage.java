package com.xiaour.wechat.mp.entity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.Serializable;

public class WxXmlOutMessage implements Serializable{

  private static final long serialVersionUID = -4135275299214187575L;

  protected String ToUserName;

  protected String FromUserName;

  protected Long CreateTime;

  protected String MsgType;
  
  protected String Content;
	 

	public String getToUserName() {
		return ToUserName;
	}


	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}


	public String getFromUserName() {
		return FromUserName;
	}


	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}


	public Long getCreateTime() {
		return CreateTime;
	}


	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}


	public String getMsgType() {
		return MsgType;
	}


	public void setMsgType(String msgType) {
		MsgType = msgType;
	}


	public String getContent() {
		return Content;
	}


	public void setContent(String content) {
		Content = content;
	}


	public String toXml() {
		XStream xStream = new XStream(new DomDriver(null,new XmlFriendlyNameCoder("_-","_")));
		String xml=xStream.toXML(this);
		xml=xml.replaceAll("com.fantong.wechat.wes.entity.WxXmlOutMessage","xml");
	    return xml;
	  }


}
