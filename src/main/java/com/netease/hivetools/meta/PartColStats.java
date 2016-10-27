
package com.netease.hivetools.meta;


public class PartColStats {

    private Long cs_id;
    private String db_name;
    private String table_name;
    private String partition_name;
    private String column_name;
    private String column_type;
    private Long part_id;
    private Long long_low_value;
    private Long long_high_value;
    private Double double_high_value;
    private Double double_low_value;
    private String big_decimal_low_value;
    private String big_decimal_high_value;
    private Long num_nulls;
    private Long num_distincts;
    private Double avg_col_len;
    private Long max_col_len;
    private Long num_trues;
    private Long num_falses;
    private Long last_analyzed;

    public void PartColStats() {
    }

    public void setCsId(Long csId_) {
        cs_id = csId_;
    }

    public Long getCsId() {
        return cs_id;
    }

    public void setDbName(String dbName_) {
        db_name = dbName_;
    }

    public String getDbName() {
        return db_name;
    }

    public void setTableName(String tableName_) {
        table_name = tableName_;
    }

    public String getTableName() {
        return table_name;
    }

    public void setPartitionName(String partitionName_) {
        partition_name = partitionName_;
    }

    public String getPartitionName() {
        return partition_name;
    }

    public void setColumnName(String columnName_) {
        column_name = columnName_;
    }

    public String getColumnName() {
        return column_name;
    }

    public void setColumnType(String columnType_) {
        column_type = columnType_;
    }

    public String getColumnType() {
        return column_type;
    }

    public void setPartId(Long partId_) {
        part_id = partId_;
    }

    public Long getPartId() {
        return part_id;
    }

    public void setLongLowValue(Long longLowValue_) {
        long_low_value = longLowValue_;
    }

    public Long getLongLowValue() {
        return long_low_value;
    }

    public void setLongHighValue(Long longHighValue_) {
        long_high_value = longHighValue_;
    }

    public Long getLongHighValue() {
        return long_high_value;
    }

    public void setDoubleHighValue(Double doubleHighValue_) {
        double_high_value = doubleHighValue_;
    }

    public Double getDoubleHighValue() {
        return double_high_value;
    }

    public void setDoubleLowValue(Double doubleLowValue_) {
        double_low_value = doubleLowValue_;
    }

    public Double getDoubleLowValue() {
        return double_low_value;
    }

    public void setBigDecimalLowValue(String bigDecimalLowValue_) {
        big_decimal_low_value = bigDecimalLowValue_;
    }

    public String getBigDecimalLowValue() {
        return big_decimal_low_value;
    }

    public void setBigDecimalHighValue(String bigDecimalHighValue_) {
        big_decimal_high_value = bigDecimalHighValue_;
    }

    public String getBigDecimalHighValue() {
        return big_decimal_high_value;
    }

    public void setNumNulls(Long numNulls_) {
        num_nulls = numNulls_;
    }

    public Long getNumNulls() {
        return num_nulls;
    }

    public void setNumDistincts(Long numDistincts_) {
        num_distincts = numDistincts_;
    }

    public Long getNumDistincts() {
        return num_distincts;
    }

    public void setAvgColLen(Double avgColLen_) {
        avg_col_len = avgColLen_;
    }

    public Double getAvgColLen() {
        return avg_col_len;
    }

    public void setMaxColLen(Long maxColLen_) {
        max_col_len = maxColLen_;
    }

    public Long getMaxColLen() {
        return max_col_len;
    }

    public void setNumTrues(Long numTrues_) {
        num_trues = numTrues_;
    }

    public Long getNumTrues() {
        return num_trues;
    }

    public void setNumFalses(Long numFalses_) {
        num_falses = numFalses_;
    }

    public Long getNumFalses() {
        return num_falses;
    }

    public void setLastAnalyzed(Long lastAnalyzed_) {
        last_analyzed = lastAnalyzed_;
    }

    public Long getLastAnalyzed() {
        return last_analyzed;
    }

}
