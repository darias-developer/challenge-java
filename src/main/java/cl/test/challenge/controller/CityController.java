package cl.test.challenge.controller;

import cl.test.challenge.data.CityInfoResponse;
import cl.test.challenge.data.TemperatureByCityResponse;
import cl.test.challenge.external.MetaWeatherExternal;
import cl.test.challenge.util.ExternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * CityController: clase que maneja el nogocio de la cuidades
 *
 * @author  David Arias
 * @version 1.0
 * @since   2021-06-28
 */
@RestController
@RequestMapping("city")
@Validated
public class CityController {

    Logger log = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private MetaWeatherExternal metaWeatherExternal;

    /**
     * Este metodo obtiene la tempratura de una ciudad
     * @param cityName nombre de la ciudad
     * @return retorna la tempretura de una ciudad
     */
    @GetMapping(value = "/temperature/{cityName}")
    @ResponseBody
    public TemperatureByCityResponse temperatureByCity(
            @PathVariable @NotBlank(message = "city.info.cityName.empty") String cityName) throws ExternalException {

        //obtiene informacion de la ciudad
        CityInfoResponse cityInfoResponse = metaWeatherExternal.getCityInfo(cityName);

        //obtiene la temperatura de la ciudad
        Double temperature = metaWeatherExternal.getTemperatureByCity(cityInfoResponse.getWoeId(), cityName);

        return new TemperatureByCityResponse(temperature, ((temperature * 9/5) + 32));
    }
}
