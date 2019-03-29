package com.creditease.ns4.gear.idgen.common;

import com.creditease.framework.scope.OutKey;

public enum InterfaceOutKey implements OutKey {

	// 接收时间
	receiveTime,
	charge,
	// 应用id
	appId,

	// 返回数据
	respData,

	// 三方跳转请求URL
	requestUrl,
	result,
	isRedirect,
	realChannel,
	;

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.toString();
	}

}
