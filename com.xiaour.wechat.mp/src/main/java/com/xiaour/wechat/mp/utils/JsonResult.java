package com.xiaour.wechat.mp.utils;

public class JsonResult {
	private Integer code=200;
	private  Object data;
	private  String message;


	public JsonResult(Integer code,String message,Object data) {
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public String toString() {
		try {
			return JsonUtil.getJsonString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String result(int code, String message){
		return JsonUtil.getJsonString(new JsonResult(code,message, ""));
	}

	public static String success(){
		return JsonUtil.getJsonString(new JsonResult(200,"操作成功", ""));
	}

	public static String fail(){
		return JsonUtil.getJsonString(new JsonResult(500,"操作失败", ""));
	}

	public static String successWithData(Object data){
		return JsonUtil.getJsonString(new JsonResult(200,"操作成功", data));
	}





}
