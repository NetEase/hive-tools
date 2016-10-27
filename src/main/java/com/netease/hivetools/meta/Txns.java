
package com.netease.hivetools.meta;


public class Txns {

    private Long txn_id;
    private String txn_state;
    private Long txn_started;
    private Long txn_last_heartbeat;
    private String txn_user;
    private String txn_host;

    public void Txns() {
    }

    public void setTxnId(Long txnId_) {
        txn_id = txnId_;
    }

    public Long getTxnId() {
        return txn_id;
    }

    public void setTxnState(String txnState_) {
        txn_state = txnState_;
    }

    public String getTxnState() {
        return txn_state;
    }

    public void setTxnStarted(Long txnStarted_) {
        txn_started = txnStarted_;
    }

    public Long getTxnStarted() {
        return txn_started;
    }

    public void setTxnLastHeartbeat(Long txnLastHeartbeat_) {
        txn_last_heartbeat = txnLastHeartbeat_;
    }

    public Long getTxnLastHeartbeat() {
        return txn_last_heartbeat;
    }

    public void setTxnUser(String txnUser_) {
        txn_user = txnUser_;
    }

    public String getTxnUser() {
        return txn_user;
    }

    public void setTxnHost(String txnHost_) {
        txn_host = txnHost_;
    }

    public String getTxnHost() {
        return txn_host;
    }

}
