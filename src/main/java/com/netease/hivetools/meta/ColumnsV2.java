
package com.netease.hivetools.meta;


public class ColumnsV2 {

    private Long cd_id;
    private String comment;
    private String column_name;
    private String type_name;
    private Long integer_idx;

    public void ColumnsV2() {
    }

    public void setCdId(Long cdId_) {
        cd_id = cdId_;
    }

    public Long getCdId() {
        return cd_id;
    }

    public void setComment(String comment_) {
        comment = comment_;
    }

    public String getComment() {
        return comment;
    }

    public void setColumnName(String columnName_) {
        column_name = columnName_;
    }

    public String getColumnName() {
        return column_name;
    }

    public void setTypeName(String typeName_) {
        type_name = typeName_;
    }

    public String getTypeName() {
        return type_name;
    }

    public void setIntegerIdx(Long integerIdx_) {
        integer_idx = integerIdx_;
    }

    public Long getIntegerIdx() {
        return integer_idx;
    }

}
