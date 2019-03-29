package com.creditease.ns4.gear.idgen.common;

/**
 * 
 * 内部数据key
 * 
 */
public enum DataExchangeKey implements com.creditease.framework.scope.ExchangeKey {

	retCode,
	retInfo,
	// 响应码
	respCode,
	// 响应信息
	respMsg,
	result,
	keyType,
	keyName,
	
	end;
	@Override
	public String getDescription() {
		return this.toString();
	}

}
