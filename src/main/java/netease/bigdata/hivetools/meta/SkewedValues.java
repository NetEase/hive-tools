
package netease.bigdata.hivetools.meta;


public class SkewedValues {

    private Long sd_id_oid;
    private Long string_list_id_eid;
    private Long integer_idx;

    public void SkewedValues() {
    }

    public void setSdIdOid(Long sdIdOid_) {
        sd_id_oid = sdIdOid_;
    }

    public Long getSdIdOid() {
        return sd_id_oid;
    }

    public void setStringListIdEid(Long stringListIdEid_) {
        string_list_id_eid = stringListIdEid_;
    }

    public Long getStringListIdEid() {
        return string_list_id_eid;
    }

    public void setIntegerIdx(Long integerIdx_) {
        integer_idx = integerIdx_;
    }

    public Long getIntegerIdx() {
        return integer_idx;
    }

}
