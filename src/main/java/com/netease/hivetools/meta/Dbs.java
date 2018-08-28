
package com.netease.hivetools.meta;


public class Dbs {

    private Long db_id;
    private String desc;
    private String db_location_uri;
    private String name;
    private String owner_name;
    private String owner_type;

    public void Dbs() {
    }

    public void setDbId(Long dbId_) {
        db_id = dbId_;
    }

    public Long getDbId() {
        return db_id;
    }

    public void setDesc(String desc_) {
        desc = desc_;
    }

    public String getDesc() {
        return desc;
    }

    public void setDbLocationUri(String dbLocationUri_) {
        db_location_uri = dbLocationUri_;
    }

    public String getDbLocationUri() {
        return db_location_uri;
    }

    public void setName(String name_) {
        name = name_;
    }

    public String getName() {
        return name;
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
