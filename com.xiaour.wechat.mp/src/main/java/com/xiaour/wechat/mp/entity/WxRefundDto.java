package com.xiaour.wechat.mp.entity;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * 微信退款
 *
 * @ClassName WxRefundDto
 * @author Zhang.Tao
 * @Date 2017年6月14日 上午9:29:01
 * @version V2.0.0
 */
public class WxRefundDto extends WxPayCommonDto {
	

	/**
	 * 商户退款单号
	 */
	private String out_refund_no;
	
	/**
	 * 退款金额
	 */
	private Integer refund_fee;

	/**
	 * 订单金额
	 */
	private Integer total_fee;
	
	/**
	 * 微信订单ID
	 */
	private String transaction_id;
	


	

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	
	public String toXml() {
		XStream xStream = new XStream(new DomDriver(null,new XmlFriendlyNameCoder("_-","_")));
		String xml=xStream.toXML(this);
		xml=xml.replaceAll("com.fantong.wechat.wes.entity.WxRefundDto","xml");
	    return xml;
	}

}
