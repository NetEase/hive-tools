
package com.netease.hivetools.meta;


public class NotificationLog {

    private Long nl_id;
    private Long event_id;
    private Long event_time;
    private String event_type;
    private String db_name;
    private String tbl_name;
    private String message;

    public void NotificationLog() {
    }

    public void setNlId(Long nlId_) {
        nl_id = nlId_;
    }

    public Long getNlId() {
        return nl_id;
    }

    public void setEventId(Long eventId_) {
        event_id = eventId_;
    }

    public Long getEventId() {
        return event_id;
    }

    public void setEventTime(Long eventTime_) {
        event_time = eventTime_;
    }

    public Long getEventTime() {
        return event_time;
    }

    public void setEventType(String eventType_) {
        event_type = eventType_;
    }

    public String getEventType() {
        return event_type;
    }

    public void setDbName(String dbName_) {
        db_name = dbName_;
    }

    public String getDbName() {
        return db_name;
    }

    public void setTblName(String tblName_) {
        tbl_name = tblName_;
    }

    public String getTblName() {
        return tbl_name;
    }

    public void setMessage(String message_) {
        message = message_;
    }

    public String getMessage() {
        return message;
    }

}
