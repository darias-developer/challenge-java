package cl.test.challenge.external;

import cl.test.challenge.data.CityInfoResponse;
import cl.test.challenge.data.CityTemperatureResponse;
import cl.test.challenge.util.ExternalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaWeatherExternal {

    Logger log = LoggerFactory.getLogger(MetaWeatherExternal.class);

    @Autowired
    private MessageSource messageSource;

    @Value("${metaweather.cityinfo.url}")
    private String cityInfoUrl;

    @Value("${metaweather.temperatureinfo.url}")
    private String temperatureInfoUrl;

    public CityInfoResponse getCityInfo(String cityName) throws ExternalException {

        log.info("init getWhereOnEarthId");

        String endpoint = cityInfoUrl + cityName;

        log.info("endpoint: " + endpoint);

        CityInfoResponse cityInfoResponse = null;

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get(endpoint).asString();

            log.info("response: " + response.getBody());

            if (response.getBody() == null) {
                throw new ExternalException(
                        messageSource.getMessage("city.info.notFound", new Object[]{cityName}, LocaleContextHolder.getLocale()));
            }

            ObjectMapper objectMapper = new ObjectMapper();

            List<CityInfoResponse> cities = objectMapper.readValue(response.getBody(), new TypeReference<List<CityInfoResponse>>(){});

            if (cities == null || cities.size() == 0) {
                throw new ExternalException(
                        messageSource.getMessage("city.info.notFound", new Object[]{cityName}, LocaleContextHolder.getLocale()));
            }

            cityInfoResponse = cities.get(0);

        } catch (UnirestException | JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new ExternalException(
                    messageSource.getMessage("city.info.error", new Object[]{endpoint}, LocaleContextHolder.getLocale()));
        }

        log.info("end getWhereOnEarthId");

        return cityInfoResponse;
    }

    public Double getTemperatureByCity(Integer whereOnEarthId, String cityName) throws ExternalException {

        log.info("init getTemperatureByCity");

        String endpoint = temperatureInfoUrl + whereOnEarthId;

        log.info("endpoint: " + endpoint);

        Double temperature = null;

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get(endpoint).asString();

            log.info("response: " + response.getBody());

            if (response.getBody() == null) {
                throw new ExternalException(
                        messageSource.getMessage("city.temperature.notFound", new Object[]{cityName}, LocaleContextHolder.getLocale()));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            CityTemperatureResponse cityTemperatureResponse = objectMapper.readValue(response.getBody(), CityTemperatureResponse.class);


            if (cityTemperatureResponse == null || cityTemperatureResponse.getConsolidatedWeather().size() == 0) {
                throw new ExternalException(
                        messageSource.getMessage("city.temperature.notFound", new Object[]{cityName}, LocaleContextHolder.getLocale()));
            }

            log.info("getTemperature: " + cityTemperatureResponse.getConsolidatedWeather().get(0).getTemperature());

            temperature = cityTemperatureResponse.getConsolidatedWeather().get(0).getTemperature();

        } catch (UnirestException | JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new ExternalException(
                    messageSource.getMessage("city.temperature.error", new Object[]{endpoint}, LocaleContextHolder.getLocale()));
        }

        log.info("end getTemperatureByCity");

        return temperature;
    }
}
