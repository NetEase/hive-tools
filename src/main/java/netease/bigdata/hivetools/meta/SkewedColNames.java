
package netease.bigdata.hivetools.meta;


public class SkewedColNames {

    private Long sd_id;
    private String skewed_col_name;
    private Long integer_idx;

    public void SkewedColNames() {
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

    public void setSkewedColName(String skewedColName_) {
        skewed_col_name = skewedColName_;
    }

    public String getSkewedColName() {
        return skewed_col_name;
    }

    public void setIntegerIdx(Long integerIdx_) {
        integer_idx = integerIdx_;
    }

    public Long getIntegerIdx() {
        return integer_idx;
    }

}
