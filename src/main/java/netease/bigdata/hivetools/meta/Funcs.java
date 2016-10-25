
package netease.bigdata.hivetools.meta;


public class Funcs {

    private Long func_id;
    private String class_name;
    private Long create_time;
    private Long db_id;
    private String func_name;
    private Long func_type;
    private String owner_name;
    private String owner_type;

    public void Funcs() {
    }

    public void setFuncId(Long funcId_) {
        func_id = funcId_;
    }

    public Long getFuncId() {
        return func_id;
    }

    public void setClassName(String className_) {
        class_name = className_;
    }

    public String getClassName() {
        return class_name;
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

    public void setFuncName(String funcName_) {
        func_name = funcName_;
    }

    public String getFuncName() {
        return func_name;
    }

    public void setFuncType(Long funcType_) {
        func_type = funcType_;
    }

    public Long getFuncType() {
        return func_type;
    }

    public void setOwnerName(String ownerName_) {
        owner_name = ownerName_;
    }

    public String getOwnerName() {
        return owner_name;
    }

    public void setOwnerType(String ownerType_) {
        owner_type = ownerType_;
    }

    public String getOwnerType() {
        return owner_type;
    }

}
