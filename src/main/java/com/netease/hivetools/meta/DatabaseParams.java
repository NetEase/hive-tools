
package com.netease.hivetools.meta;


public class DatabaseParams {

    private Long db_id;
    private String param_key;
    private String param_value;

    public void DatabaseParams() {
    }

    public void setDbId(Long dbId_) {
        db_id = dbId_;
    }

    public Long getDbId() {
        return db_id;
    }

    public void setParamKey(String paramKey_) {
        param_key = paramKey_;
    }

    public String getParamKey() {
        return param_key;
    }

    public void setParamValue(String paramValue_) {
        param_value = paramValue_;
    }

    public String getParamValue() {
        return param_value;
    }

}
