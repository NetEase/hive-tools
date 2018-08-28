
package com.netease.hivetools.meta;


public class SdParams {

    private Long sd_id;
    private String param_key;
    private String param_value;

    public void SdParams() {
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
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
