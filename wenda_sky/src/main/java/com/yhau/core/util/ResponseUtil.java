package com.yhau.core.util;

import java.io.Serializable;

public class ResponseUtil implements Serializable {

	private static final long serialVersionUID = 6099134931358707391L;
	
	private boolean success = true;
	private String msg  = "";
	private Integer code;
	
	private ResponseUtil() {

	}
	
	private ResponseUtil(Integer code) {
		this.code = code;
	}
	
	/**
	 * 返回成功响应
	 */
	public static ResponseUtil ok() {
		return new ResponseUtil();
	}
	
	/**
	 * 返回成功响应,携带数据
	 */
	public static ResponseUtil ok(Integer code) {
		return new ResponseUtil(code);
	}
	
	public static ResponseUtil fail(String msg) {
		ResponseUtil response = new ResponseUtil();
		response.success = false;
		response.msg = msg;
		return response;
	}
	
	public static ResponseUtil fail(String msg, Integer code) {
		ResponseUtil response = new ResponseUtil(code);
		response.success = false;
		response.msg = msg;
		return response;
	}
	
	public Integer getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public boolean isSuccess() {
		return  success;
	}

}
