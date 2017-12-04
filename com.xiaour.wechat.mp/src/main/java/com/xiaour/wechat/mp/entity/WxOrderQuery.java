package com.xiaour.wechat.mp.entity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class WxOrderQuery extends WxPayCommonDto {
	
	private String transaction_id;

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	
	
	public String toXml() {
		XStream xStream = new XStream(new DomDriver(null,new XmlFriendlyNameCoder("_-","_")));
		String xml=xStream.toXML(this);
		xml=xml.replaceAll("com.fantong.wechat.wes.entity.WxOrderQuery","xml");
	    return xml;
	}
	
}
