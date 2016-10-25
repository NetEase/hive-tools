
package netease.bigdata.hivetools.meta;


public class TypeFields {

    private Long type_name;
    private String comment;
    private String field_name;
    private String field_type;
    private Long integer_idx;

    public void TypeFields() {
    }

    public void setTypeName(Long typeName_) {
        type_name = typeName_;
    }

    public Long getTypeName() {
        return type_name;
    }

    public void setComment(String comment_) {
        comment = comment_;
    }

    public String getComment() {
        return comment;
    }

    public void setFieldName(String fieldName_) {
        field_name = fieldName_;
    }

    public String getFieldName() {
        return field_name;
    }

    public void setFieldType(String fieldType_) {
        field_type = fieldType_;
    }

    public String getFieldType() {
        return field_type;
    }

    public void setIntegerIdx(Long integerIdx_) {
        integer_idx = integerIdx_;
    }

    public Long getIntegerIdx() {
        return integer_idx;
    }

}
