
package netease.bigdata.hivetools.meta;


public class RoleMap {

    private Long role_grant_id;
    private Long add_time;
    private Integer grant_option;
    private String grantor;
    private String grantor_type;
    private String principal_name;
    private String principal_type;
    private Long role_id;

    public void RoleMap() {
    }

    public void setRoleGrantId(Long roleGrantId_) {
        role_grant_id = roleGrantId_;
    }

    public Long getRoleGrantId() {
        return role_grant_id;
    }

    public void setAddTime(Long addTime_) {
        add_time = addTime_;
    }

    public Long getAddTime() {
        return add_time;
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

    public void setRoleId(Long roleId_) {
        role_id = roleId_;
    }

    public Long getRoleId() {
        return role_id;
    }

}
