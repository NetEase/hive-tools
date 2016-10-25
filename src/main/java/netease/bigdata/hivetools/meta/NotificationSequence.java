
package netease.bigdata.hivetools.meta;


public class NotificationSequence {

    private Long nni_id;
    private Long next_event_id;

    public void NotificationSequence() {
    }

    public void setNniId(Long nniId_) {
        nni_id = nniId_;
    }

    public Long getNniId() {
        return nni_id;
    }

    public void setNextEventId(Long nextEventId_) {
        next_event_id = nextEventId_;
    }

    public Long getNextEventId() {
        return next_event_id;
    }

}
