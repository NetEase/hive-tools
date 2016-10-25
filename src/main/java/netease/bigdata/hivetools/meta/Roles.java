
package netease.bigdata.hivetools.meta;


public class Roles {

    private Long role_id;
    private Long create_time;
    private String owner_name;
    private String role_name;

    public void Roles() {
    }

    public void setRoleId(Long roleId_) {
        role_id = roleId_;
    }

    public Long getRoleId() {
        return role_id;
    }

    public void setCreateTime(Long createTime_) {
        create_time = createTime_;
    }

    public Long getCreateTime() {
        return create_time;
    }

    public void setOwnerName(String ownerName_) {
        owner_name = ownerName_;
    }

    public String getOwnerName() {
        return owner_name;
    }

    public void setRoleName(String roleName_) {
        role_name = roleName_;
    }

    public String getRoleName() {
        return role_name;
    }

}
