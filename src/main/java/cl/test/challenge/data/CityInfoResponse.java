package cl.test.challenge.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CityInfoResponse {

    private String title;
    private String locationType;
    private Integer woeId;
    private String coordinates;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("location_type")
    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @JsonProperty("woeid")
    public Integer getWoeId() {
        return woeId;
    }

    public void setWoeId(Integer woeId) {
        this.woeId = woeId;
    }

    @JsonProperty("latt_long")
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
