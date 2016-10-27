
package com.netease.hivetools.meta;


public class SerdeParams {

    private Long serde_id;
    private String param_key;
    private String param_value;

    public void SerdeParams() {
    }

    public void setSerdeId(Long serdeId_) {
        serde_id = serdeId_;
    }

    public Long getSerdeId() {
        return serde_id;
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
