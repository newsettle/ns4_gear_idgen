package com.creditease.ns4.gear.idgen.service.impl;

import java.util.List;

import javax.sql.DataSource;

import com.creditease.ns4.gear.idgen.entity.BaseKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.creditease.ns4.gear.idgen.service.BaseDao;

@Service("baseDao")
public class BaseDaoImpl implements BaseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<BaseKeys> findAllKeyInfo() {
        return getJdbcTemplate().query("select id, key_name, key_value, key_length, key_cache, key_prefix, key_suffix, key_digit, version from gear_key", new BaseKeys());
    }

    @Override
    public List<BaseKeys> findKeyInfoByName(String keyName) {
        return getJdbcTemplate().query("select key_name, key_value, key_length, key_cache, key_prefix, key_suffix, key_digit, version FROM gear_key WHERE key_name = ?", new Object[]{keyName}, new BaseKeys());
    }

    @Override
    public int updateKeyInfoValue(long value, String keyName, int version) {
        return getJdbcTemplate().update("update gear_key set key_value = ?, version = version + 1 where key_name = ? and version = ?", value, keyName, version);
    }


}
