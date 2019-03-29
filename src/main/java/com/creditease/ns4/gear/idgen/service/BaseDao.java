package com.creditease.ns4.gear.idgen.service;

import java.util.List;

import com.creditease.ns4.gear.idgen.entity.BaseKeys;
import org.springframework.jdbc.core.JdbcTemplate;

public interface BaseDao {

	JdbcTemplate getJdbcTemplate();
	
	List<BaseKeys> findAllKeyInfo();

	List<BaseKeys> findKeyInfoByName(String keyName);
	
	int updateKeyInfoValue(long max, String keyName, int version);
}
