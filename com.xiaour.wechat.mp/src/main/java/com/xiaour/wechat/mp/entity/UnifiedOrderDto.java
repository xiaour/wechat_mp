package com.xiaour.wechat.mp.entity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 *	微信支付统一下单DTO
 * @ClassName UnifiedOrderDto
 * @author Zhang.Tao
 * @Date 2017年6月13日 下午3:08:12
 * @version V2.0.0
 */
public class UnifiedOrderDto extends WxPayCommonDto {
	
	
	private String attach;
	
	private String body;
	
	private String detail;
	
	private String notify_url;
	
	private String openid;
	
	private String out_trade_no;
	
	private String spbill_create_ip;
	
	private String total_fee;
	
	private String trade_type;
	
	


	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}


	
	public String toXml() {
		XStream xStream = new XStream(new DomDriver(null,new XmlFriendlyNameCoder("_-","_")));
		String xml=xStream.toXML(this);
		xml=xml.replaceAll("com.fantong.wechat.wes.entity.UnifiedOrderDto","xml");
	    return xml;
	}
	
	public static void main(String[] args) {
		UnifiedOrderDto dto= new UnifiedOrderDto();
		dto.setAppid("111");
		dto.setAttach("AES1231");
		dto.setBody("EWS1111");
		dto.setDetail("{\111\":\"222\"}");
		dto.setNonce_str("C3P01dFStr0f");
		dto.setNotify_url("http://127.0.0.1");
		dto.setOpenid("OPENID00001");
		dto.setOut_trade_no("OTN-001");
		dto.setSign("DKJHSDI123H4A");
		dto.setTotal_fee("1550");
		dto.setTrade_type("JSAPI");
		System.out.println(dto.toXml());
	}

}
