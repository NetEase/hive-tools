
package netease.bigdata.hivetools.meta;


public class TblColPrivs {

    private Long tbl_column_grant_id;
    private String column_name;
    private Long create_time;
    private Integer grant_option;
    private String grantor;
    private String grantor_type;
    private String principal_name;
    private String principal_type;
    private String tbl_col_priv;
    private Long tbl_id;

    public void TblColPrivs() {
    }

    public void setTblColumnGrantId(Long tblColumnGrantId_) {
        tbl_column_grant_id = tblColumnGrantId_;
    }

    public Long getTblColumnGrantId() {
        return tbl_column_grant_id;
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

    public void setTblColPriv(String tblColPriv_) {
        tbl_col_priv = tblColPriv_;
    }

    public String getTblColPriv() {
        return tbl_col_priv;
    }

    public void setTblId(Long tblId_) {
        tbl_id = tblId_;
    }

    public Long getTblId() {
        return tbl_id;
    }

}
