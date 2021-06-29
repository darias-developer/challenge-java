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

/**
 * MetaWeatherExternal: clase que maneja la conexion con la api de MetaWeather
 *
 * @author  David Arias
 * @version 1.0
 * @since   2021-06-28
 */
@Service
public class MetaWeatherExternal {

    Logger log = LoggerFactory.getLogger(MetaWeatherExternal.class);

    @Autowired
    private MessageSource messageSource;

    @Value("${metaweather.cityinfo.url}")
    private String cityInfoUrl;

    @Value("${metaweather.temperatureinfo.url}")
    private String temperatureInfoUrl;

    /**
     * Este metodo obtiene la informacion de una ciudad
     * @param cityName nombre de la ciudad
     * @return retorna la informacion de una ciudad
     */
    public CityInfoResponse getCityInfo(String cityName) throws ExternalException {

        log.info("init getWhereOnEarthId");

        CityInfoResponse cityInfoResponse = null;

        try {

            if (cityName == null || cityName.trim().equals("")) {
                throw new ExternalException(
                        messageSource.getMessage("city.info.cityName.empty", null, LocaleContextHolder.getLocale()));
            }

            String endpoint = cityInfoUrl + cityName;

            log.info("endpoint: " + endpoint);

            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get(endpoint).asString();

            if (response.getCode() != 200) {
                throw new ExternalException(
                        messageSource.getMessage("city.info.error", null, LocaleContextHolder.getLocale()));
            }

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
                    messageSource.getMessage("city.info.error", null, LocaleContextHolder.getLocale()));
        }

        log.info("end getWhereOnEarthId");

        return cityInfoResponse;
    }

    /**
     * Este metodo obtiene la temperatura de una ciudad
     * @param whereOnEarthId id de la ciudad en metaweather
     * @param cityName nombre de la ciudad
     * @return retorna la temperatura de una ciudad en centigrados
     */
    public Double getTemperatureByCity(Integer whereOnEarthId, String cityName) throws ExternalException {

        log.info("init getTemperatureByCity");

        Double temperature = null;

        try {

            if (cityName == null || cityName.trim().equals("")) {
                throw new ExternalException(
                        messageSource.getMessage("city.info.cityName.empty", null, LocaleContextHolder.getLocale()));
            }

            if (whereOnEarthId == null) {
                throw new ExternalException(
                        messageSource.getMessage("city.temperature.whereOnEarthId.empty", null, LocaleContextHolder.getLocale()));
            }

            String endpoint = temperatureInfoUrl + whereOnEarthId;

            log.info("endpoint: " + endpoint);

            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.get(endpoint).asString();

            if (response.getCode() != 200) {
                throw new ExternalException(
                        messageSource.getMessage("city.temperature.error", null, LocaleContextHolder.getLocale()));
            }

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
                    messageSource.getMessage("city.temperature.error", null, LocaleContextHolder.getLocale()));
        }

        log.info("end getTemperatureByCity");

        return temperature;
    }
}
