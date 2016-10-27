
package com.netease.hivetools.meta;


public class SequenceTable {

    private String sequence_name;
    private Long next_val;

    public void SequenceTable() {
    }

    public void setSequenceName(String sequenceName_) {
        sequence_name = sequenceName_;
    }

    public String getSequenceName() {
        return sequence_name;
    }

    public void setNextVal(Long nextVal_) {
        next_val = nextVal_;
    }

    public Long getNextVal() {
        return next_val;
    }

}
