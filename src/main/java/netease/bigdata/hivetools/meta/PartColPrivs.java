
package netease.bigdata.hivetools.meta;


public class PartColPrivs {

    private Long part_column_grant_id;
    private String column_name;
    private Long create_time;
    private Integer grant_option;
    private String grantor;
    private String grantor_type;
    private Long part_id;
    private String principal_name;
    private String principal_type;
    private String part_col_priv;

    public void PartColPrivs() {
    }

    public void setPartColumnGrantId(Long partColumnGrantId_) {
        part_column_grant_id = partColumnGrantId_;
    }

    public Long getPartColumnGrantId() {
        return part_column_grant_id;
    }

    public void setColumnName(String columnName_) {
        column_name = columnName_;
    }

    public String getColumnName() {
        return column_name;
    }

    public void setCreateTime(Long createTime_) {
        create_time = createTime_;
    }

    public Long getCreateTime() {
        return create_time;
    }

    public void setGrantOption(Integer grantOption_) {
        grant_option = grantOption_;
    }

    public Integer getGrantOption() {
        return grant_option;
    }

    public void setGrantor(String grantor_) {
        grantor = grantor_;
    }

    public String getGrantor() {
        return grantor;
    }

    public void setGrantorType(String grantorType_) {
        grantor_type = grantorType_;
    }

    public String getGrantorType() {
        return grantor_type;
    }

    public void setPartId(Long partId_) {
        part_id = partId_;
    }

    public Long getPartId() {
        return part_id;
    }

    public void setPrincipalName(String principalName_) {
        principal_name = principalName_;
    }

    public String getPrincipalName() {
        return principal_name;
    }

    public void setPrincipalType(String principalType_) {
        principal_type = principalType_;
    }

    public String getPrincipalType() {
        return principal_type;
    }

    public void setPartColPriv(String partColPriv_) {
        part_col_priv = partColPriv_;
    }

    public String getPartColPriv() {
        return part_col_priv;
    }

}
