package cl.test.challenge.data;

public class TemperatureByCityResponse {

    private Double centigrade;
    private Double fahrenheit;

    public TemperatureByCityResponse() {

    }

    public TemperatureByCityResponse(Double centigrade, Double fahrenheit) {
        this.centigrade = centigrade;
        this.fahrenheit = fahrenheit;
    }

    public Double getCentigrade() {
        return centigrade;
    }

    public void setCentigrade(Double centigrade) {
        this.centigrade = centigrade;
    }

    public Double getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(Double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }
}
