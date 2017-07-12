package nickita.gq.yula.model;

/**
 * Created by admin on 10/7/17.
 */
public class GeoTag {

    private String userid;
    private double lat;
    private double lng;
    private String tagid;
    private String description;
    private boolean status;
    private String date;

    public GeoTag(String userid, double lat, double lng, String tagid, String description, boolean status, String date) {
        this.userid = userid;
        this.lat = lat;
        this.lng = lng;
        this.tagid = tagid;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
