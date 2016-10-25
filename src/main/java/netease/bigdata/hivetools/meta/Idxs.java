
package netease.bigdata.hivetools.meta;


public class Idxs {

    private Long index_id;
    private Long create_time;
    private Boolean deferred_rebuild;
    private String index_handler_class;
    private String index_name;
    private Long index_tbl_id;
    private Long last_access_time;
    private Long orig_tbl_id;
    private Long sd_id;

    public void Idxs() {
    }

    public void setIndexId(Long indexId_) {
        index_id = indexId_;
    }

    public Long getIndexId() {
        return index_id;
    }

    public void setCreateTime(Long createTime_) {
        create_time = createTime_;
    }

    public Long getCreateTime() {
        return create_time;
    }

    public void setDeferredRebuild(Boolean deferredRebuild_) {
        deferred_rebuild = deferredRebuild_;
    }

    public Boolean getDeferredRebuild() {
        return deferred_rebuild;
    }

    public void setIndexHandlerClass(String indexHandlerClass_) {
        index_handler_class = indexHandlerClass_;
    }

    public String getIndexHandlerClass() {
        return index_handler_class;
    }

    public void setIndexName(String indexName_) {
        index_name = indexName_;
    }

    public String getIndexName() {
        return index_name;
    }

    public void setIndexTblId(Long indexTblId_) {
        index_tbl_id = indexTblId_;
    }

    public Long getIndexTblId() {
        return index_tbl_id;
    }

    public void setLastAccessTime(Long lastAccessTime_) {
        last_access_time = lastAccessTime_;
    }

    public Long getLastAccessTime() {
        return last_access_time;
    }

    public void setOrigTblId(Long origTblId_) {
        orig_tbl_id = origTblId_;
    }

    public Long getOrigTblId() {
        return orig_tbl_id;
    }

    public void setSdId(Long sdId_) {
        sd_id = sdId_;
    }

    public Long getSdId() {
        return sd_id;
    }

}
