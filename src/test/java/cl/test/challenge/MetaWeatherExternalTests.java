package cl.test.challenge;

import cl.test.challenge.data.CityInfoResponse;
import cl.test.challenge.external.MetaWeatherExternal;
import cl.test.challenge.util.ExternalException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MetaWeatherExternalTests {

    @Autowired
    private MetaWeatherExternal metaWeatherExternal;

    @Test
    void getCityInfo_cityName_empty() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getCityInfo("");
        });

        String expectedMessage = "Debe ingresar el nombre de la ciudad";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCityInfo_cityName_only_space() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getCityInfo("  ");
        });

        String expectedMessage = "Debe ingresar el nombre de la ciudad";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCityInfo_cityName_null() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getCityInfo(null);
        });

        String expectedMessage = "Debe ingresar el nombre de la ciudad";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCityInfo_cityName_notValid() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getCityInfo("jdshfjkshfkjhsdkjfhsdkjfhsjd");
        });

        String expectedMessage = "no fue encontrada";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getCityInfo_cityName_valid() throws ExternalException {

        CityInfoResponse cityInfoResponse = metaWeatherExternal.getCityInfo("santiago");

        assertTrue(cityInfoResponse.getTitle().toLowerCase().contains("santiago"));
    }

    @Test
    void getTemperatureByCity_whereOnEarthId_and_cityName_empty() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getTemperatureByCity(null, "");
        });

        String expectedMessage = "Debe ingresar el nombre de la ciudad";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getTemperatureByCity_whereOnEarthId_empty() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getTemperatureByCity(null, "test");
        });

        String expectedMessage = "Debe ingresar el whereOnEarthId de la ciudad";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getTemperatureByCity_whereOnEarthId_not_valid() {

        Exception exception = assertThrows(ExternalException.class, () -> {
            metaWeatherExternal.getTemperatureByCity(-1, "test");
        });

        String expectedMessage = "Ha ocurrido un error en la llamada del servicio";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getTemperatureByCity_whereOnEarthId_valid() throws ExternalException {

        Double temp = metaWeatherExternal.getTemperatureByCity(349859, "santiago");

        assertFalse(temp.isNaN());
    }
}
