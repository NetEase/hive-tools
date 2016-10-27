
package com.netease.hivetools.meta;


public class BucketingCols {

    private Long sd_id;
    private String bucket_col_name;
    private Long integer_idx;

    public void BucketingCols() {
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

    public void setBucketColName(String bucketColName_) {
        bucket_col_name = bucketColName_;
    }

    public String getBucketColName() {
        return bucket_col_name;
    }

    public void setIntegerIdx(Long integerIdx_) {
        integer_idx = integerIdx_;
    }

    public Long getIntegerIdx() {
        return integer_idx;
    }

}
