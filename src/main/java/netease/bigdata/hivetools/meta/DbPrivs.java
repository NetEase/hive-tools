
package netease.bigdata.hivetools.meta;


public class DbPrivs {

    private Long db_grant_id;
    private Long create_time;
    private Long db_id;
    private Integer grant_option;
    private String grantor;
    private String grantor_type;
    private String principal_name;
    private String principal_type;
    private String db_priv;

    public void DbPrivs() {
    }

    public void setDbGrantId(Long dbGrantId_) {
        db_grant_id = dbGrantId_;
    }

    public Long getDbGrantId() {
        return db_grant_id;
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

    public void setDbPriv(String dbPriv_) {
        db_priv = dbPriv_;
    }

    public String getDbPriv() {
        return db_priv;
    }

}
