
package com.netease.hivetools.meta;


public class TableParams {

    private Long tbl_id;
    private String param_key;
    private String param_value;

    public void TableParams() {
    }

    public void setTblId(Long tblId_) {
        tbl_id = tblId_;
    }

    public Long getTblId() {
        return tbl_id;
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
