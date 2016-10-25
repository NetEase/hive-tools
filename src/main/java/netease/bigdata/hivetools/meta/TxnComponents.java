
package netease.bigdata.hivetools.meta;


public class TxnComponents {

    private Long tc_txnid;
    private String tc_database;
    private String tc_table;
    private String tc_partition;

    public void TxnComponents() {
    }

    public void setTcTxnid(Long tcTxnid_) {
        tc_txnid = tcTxnid_;
    }

    public Long getTcTxnid() {
        return tc_txnid;
    }

    public void setTcDatabase(String tcDatabase_) {
        tc_database = tcDatabase_;
    }

    public String getTcDatabase() {
        return tc_database;
    }

    public void setTcTable(String tcTable_) {
        tc_table = tcTable_;
    }

    public String getTcTable() {
        return tc_table;
    }

    public void setTcPartition(String tcPartition_) {
        tc_partition = tcPartition_;
    }

    public String getTcPartition() {
        return tc_partition;
    }

}
