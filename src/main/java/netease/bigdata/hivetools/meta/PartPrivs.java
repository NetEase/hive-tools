
package netease.bigdata.hivetools.meta;


public class PartPrivs {

    private Long part_grant_id;
    private Long create_time;
    private Integer grant_option;
    private String grantor;
    private String grantor_type;
    private Long part_id;
    private String principal_name;
    private String principal_type;
    private String part_priv;

    public void PartPrivs() {
    }

    public void setPartGrantId(Long partGrantId_) {
        part_grant_id = partGrantId_;
    }

    public Long getPartGrantId() {
        return part_grant_id;
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

    public void setPartPriv(String partPriv_) {
        part_priv = partPriv_;
    }

    public String getPartPriv() {
        return part_priv;
    }

}
