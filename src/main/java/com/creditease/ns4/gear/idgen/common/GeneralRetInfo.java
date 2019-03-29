package com.creditease.ns4.gear.idgen.common;

import com.creditease.framework.scope.RetInfo;

public class GeneralRetInfo implements RetInfo {
	private String code = "0000";
	private String msg = "获取成功";

	public GeneralRetInfo() {

	}

	public GeneralRetInfo(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public GeneralRetInfo(String code) {
		this.code = code;
	}


	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}
}
