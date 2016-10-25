
package netease.bigdata.hivetools.meta;


public class MasterKeys {

    private Long key_id;
    private String master_key;

    public void MasterKeys() {
    }

    public void setKeyId(Long keyId_) {
        key_id = keyId_;
    }

    public Long getKeyId() {
        return key_id;
    }

    public void setMasterKey(String masterKey_) {
        master_key = masterKey_;
    }

    public String getMasterKey() {
        return master_key;
    }

}
