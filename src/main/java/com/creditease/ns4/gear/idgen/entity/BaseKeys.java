package com.creditease.ns4.gear.idgen.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jdbc.core.RowMapper;


/**
 * 数据库信息
 */
public class BaseKeys implements RowMapper<BaseKeys>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5336766680317040531L;

    /**
     * identifier field
     */
    private String keyName;

    private String system;

    /**
     * persistent field
     */
    private String keyValue;

    /**
     * persistent field
     */
    private Integer keyLength;

    /**
     * persistent field
     */
    private Integer keyCache;

    /**
     * nullable persistent field
     */
    private String keyPrefix;

    /**
     * nullable persistent field
     */
    private String keySuffix;

    private String keyDigit;

    private Integer version;


    /**
     * default constructor
     */
    public BaseKeys() {
    }


    @Override
    public BaseKeys mapRow(ResultSet rs, int rowNum) throws SQLException {
        BaseKeys baseKey = new BaseKeys();
        baseKey.setKeyName(rs.getString("key_name"));
        baseKey.setKeyValue(rs.getString("key_value"));
        baseKey.setKeyLength(rs.getInt("key_length"));
        baseKey.setKeyCache(rs.getInt("key_cache"));
        baseKey.setKeyPrefix(rs.getString("key_prefix"));
        baseKey.setKeySuffix(rs.getString("key_suffix"));
        baseKey.setKeyDigit(rs.getString("key_digit"));
        baseKey.setVersion(rs.getInt("version"));
        return baseKey;
    }


    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }


    public String getSystem() {
        return system;
    }


    public void setSystem(String system) {
        this.system = system;
    }


    public String getKeyValue() {
        return keyValue;
    }


    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }


    public Integer getKeyLength() {
        return this.keyLength;
    }

    public void setKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
    }

    public Integer getKeyCache() {
        return this.keyCache;
    }

    public void setKeyCache(Integer keyCache) {
        this.keyCache = keyCache;
    }

    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKeySuffix() {
        return this.keySuffix;
    }

    public void setKeySuffix(String keySuffix) {
        this.keySuffix = keySuffix;
    }


    public String getKeyDigit() {
        return keyDigit;
    }


    public void setKeyDigit(String keyDigit) {
        this.keyDigit = keyDigit;
    }

    public Integer getVersion() {
        return version;
    }


    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("keyName", getKeyName())
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BaseKeys)) {
            return false;
        }
        BaseKeys castOther = (BaseKeys) other;
        return new EqualsBuilder()
                .append(this.getKeyName(), castOther.getKeyName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getKeyName())
                .toHashCode();
    }

}
