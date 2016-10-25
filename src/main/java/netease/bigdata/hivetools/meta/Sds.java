
package netease.bigdata.hivetools.meta;


public class Sds {

    private Long sd_id;
    private Long cd_id;
    private String input_format;
    private Boolean is_compressed;
    private Boolean is_storedassubdirectories;
    private String location;
    private Long num_buckets;
    private String output_format;
    private Long serde_id;

    public void Sds() {
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

    public void setCdId(Long cdId_) {
        cd_id = cdId_;
    }

    public Long getCdId() {
        return cd_id;
    }

    public void setInputFormat(String inputFormat_) {
        input_format = inputFormat_;
    }

    public String getInputFormat() {
        return input_format;
    }

    public void setIsCompressed(Boolean isCompressed_) {
        is_compressed = isCompressed_;
    }

    public Boolean getIsCompressed() {
        return is_compressed;
    }

    public void setIsStoredassubdirectories(Boolean isStoredassubdirectories_) {
        is_storedassubdirectories = isStoredassubdirectories_;
    }

    public Boolean getIsStoredassubdirectories() {
        return is_storedassubdirectories;
    }

    public void setLocation(String location_) {
        location = location_;
    }

    public String getLocation() {
        return location;
    }

    public void setNumBuckets(Long numBuckets_) {
        num_buckets = numBuckets_;
    }

    public Long getNumBuckets() {
        return num_buckets;
    }

    public void setOutputFormat(String outputFormat_) {
        output_format = outputFormat_;
    }

    public String getOutputFormat() {
        return output_format;
    }

    public void setSerdeId(Long serdeId_) {
        serde_id = serdeId_;
    }

    public Long getSerdeId() {
        return serde_id;
    }

}
