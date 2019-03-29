package com.creditease.ns4.gear.idgen.service;

import com.creditease.ns4.gear.idgen.cache.KeyInfo;

public interface KeyInfoService {

	String createKey(String name) throws Exception;
	
	String createKey(String name, String prefix, String suffix, int length) throws Exception;
	
	long createKeyAsLong(String name) throws Exception;
	
	void putKeyInfo(String key, KeyInfo keyInfo);
}
