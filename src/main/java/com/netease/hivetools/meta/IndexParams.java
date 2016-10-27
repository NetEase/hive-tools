
package com.netease.hivetools.meta;


public class IndexParams {

    private Long index_id;
    private String param_key;
    private String param_value;

    public void IndexParams() {
    }

    public void setIndexId(Long indexId_) {
        index_id = indexId_;
    }

    public Long getIndexId() {
        return index_id;
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
