package cl.test.challenge.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityTemperatureResponse {

    private List<Temperature> consolidatedWeather;

    @JsonProperty("consolidated_weather")
    public List<Temperature> getConsolidatedWeather() {
        return consolidatedWeather;
    }

    public void setConsolidatedWeather(List<Temperature> consolidatedWeather) {
        this.consolidatedWeather = consolidatedWeather;
    }

    public static class Temperature {

        private Double temperature;

        @JsonProperty("the_temp")
        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
    }
}
