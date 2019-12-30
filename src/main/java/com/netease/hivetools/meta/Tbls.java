
package com.netease.hivetools.meta;


public class Tbls {

    private Long tbl_id;
    private Long create_time;
    private Long db_id;
    private Long last_access_time;
    private String owner;
    private Long retention;
    private Long sd_id;
    private String tbl_name;
    private String tbl_type;
    private String view_expanded_text;
    private String view_original_text;
    private Long link_target_id;

    public void Tbls() {
    }

    public void setTblId(Long tblId_) {
        tbl_id = tblId_;
    }

    public Long getTblId() {
        return tbl_id;
    }

    public void setCreateTime(Long createTime_) {
        create_time = createTime_;
    }

    public Long getCreateTime() {
        return create_time;
    }

    public void setDbId(Long dbId_) {
        db_id = dbId_;
    }

    public Long getDbId() {
        return db_id;
    }

    public void setLastAccessTime(Long lastAccessTime_) {
        last_access_time = lastAccessTime_;
    }

    public Long getLastAccessTime() {
        return last_access_time;
    }

    public void setOwner(String owner_) {
        owner = owner_;
    }

    public String getOwner() {
        return owner;
    }

    public void setRetention(Long retention_) {
        retention = retention_;
    }

    public Long getRetention() {
        return retention;
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

    public void setTblName(String tblName_) {
        tbl_name = tblName_;
    }

    public String getTblName() {
        return tbl_name;
    }

    public void setTblType(String tblType_) {
        tbl_type = tblType_;
    }

    public String getTblType() {
        return tbl_type;
    }

    public void setViewExpandedText(String viewExpandedText_) {
        view_expanded_text = viewExpandedText_;
    }

    public String getViewExpandedText() {
        return view_expanded_text;
    }

    public void setZZViewOriginalText(String viewOriginalText_) {
        view_original_text = viewOriginalText_;
    }

    public String getViewOriginalText() {
        return view_original_text;
    }

    public void setLinkTargetId(Long linkTargetId_) {
        link_target_id = linkTargetId_;
    }

    public Long getLinkTargetId() {
        return link_target_id;
    }

}
