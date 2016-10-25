
package netease.bigdata.hivetools.meta;


public class Serdes {

    private Long serde_id;
    private String name;
    private String slib;

    public void Serdes() {
    }

    public void setSerdeId(Long serdeId_) {
        serde_id = serdeId_;
    }

    public Long getSerdeId() {
        return serde_id;
    }

    public void setName(String name_) {
        name = name_;
    }

    public String getName() {
        return name;
    }

    public void setSlib(String slib_) {
        slib = slib_;
    }

    public String getSlib() {
        return slib;
    }

}
