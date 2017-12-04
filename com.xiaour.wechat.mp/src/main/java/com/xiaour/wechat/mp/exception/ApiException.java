package com.xiaour.wechat.mp.exception;

public class ApiException extends Exception {

	/**
	 */
	private static final long serialVersionUID = 1L;

	public ApiException(int errCode, String errMsg) {
		super("error code: " + errCode + ", error message: " + errMsg);
	}

	public ApiException(Exception e) {
		super(e);
	}
}
