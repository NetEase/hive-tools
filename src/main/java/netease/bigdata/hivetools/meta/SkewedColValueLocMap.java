
package netease.bigdata.hivetools.meta;


public class SkewedColValueLocMap {

    private Long sd_id;
    private Long string_list_id_kid;
    private String location;

    public void SkewedColValueLocMap() {
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

    public void setStringListIdKid(Long stringListIdKid_) {
        string_list_id_kid = stringListIdKid_;
    }

    public Long getStringListIdKid() {
        return string_list_id_kid;
    }

    public void setLocation(String location_) {
        location = location_;
    }

    public String getLocation() {
        return location;
    }

}
