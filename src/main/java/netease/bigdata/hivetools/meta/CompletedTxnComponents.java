
package netease.bigdata.hivetools.meta;


public class CompletedTxnComponents {

    private Long ctc_txnid;
    private String ctc_database;
    private String ctc_table;
    private String ctc_partition;

    public void CompletedTxnComponents() {
    }

    public void setCtcTxnid(Long ctcTxnid_) {
        ctc_txnid = ctcTxnid_;
    }

    public Long getCtcTxnid() {
        return ctc_txnid;
    }

    public void setCtcDatabase(String ctcDatabase_) {
        ctc_database = ctcDatabase_;
    }

    public String getCtcDatabase() {
        return ctc_database;
    }

    public void setCtcTable(String ctcTable_) {
        ctc_table = ctcTable_;
    }

    public String getCtcTable() {
        return ctc_table;
    }

    public void setCtcPartition(String ctcPartition_) {
        ctc_partition = ctcPartition_;
    }

    public String getCtcPartition() {
        return ctc_partition;
    }

}
