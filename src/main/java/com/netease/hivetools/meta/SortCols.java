
package com.netease.hivetools.meta;


public class SortCols {

    private Long sd_id;
    private String column_name;
    private Long order;
    private Long integer_idx;

    public void SortCols() {
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

    public void setColumnName(String columnName_) {
        column_name = columnName_;
    }

    public String getColumnName() {
        return column_name;
    }

    public void setOrder(Long order_) {
        order = order_;
    }

    public Long getOrder() {
        return order;
    }

    public void setIntegerIdx(Long integerIdx_) {
        integer_idx = integerIdx_;
    }

    public Long getIntegerIdx() {
        return integer_idx;
    }

}
