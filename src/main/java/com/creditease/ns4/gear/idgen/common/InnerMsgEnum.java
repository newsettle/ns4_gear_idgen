package com.creditease.ns4.gear.idgen.common;

public enum InnerMsgEnum {

	GOT_SUCCESS("0000","获取成功"),
	GOT_FAIL("9999","获取失败，请检查参数或重试"),
	;
	
	private InnerMsgEnum (String retCode,String retInfo){
		this.retCode =retCode;
		this.retInfo =retInfo;
	}
	
	private String retCode;
	private String retInfo;
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetInfo() {
		return retInfo;
	}
	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}
	
}
