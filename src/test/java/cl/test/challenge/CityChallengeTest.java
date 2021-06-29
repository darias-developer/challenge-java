package cl.test.challenge;

import cl.test.challenge.controller.CityController;
import cl.test.challenge.data.CityInfoResponse;
import cl.test.challenge.external.MetaWeatherExternal;
import cl.test.challenge.util.ExternalException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CityController.class)
@ActiveProfiles("test")
public class CityChallengeTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetaWeatherExternal metaWeatherExternal;

    @Test
    void temperatureByCity_cityName_empty() throws Exception {

        given(metaWeatherExternal.getCityInfo(Mockito.any())).willReturn(null);

        this.mockMvc.perform(get("/city/temperature/{cityName}", ""))
                .andExpect(status().isNotFound());
    }

    @Test
    void temperatureByCity_cityName_valid() throws Exception {

        CityInfoResponse cityInfoResponse = new CityInfoResponse();

        given(metaWeatherExternal.getCityInfo(Mockito.any())).willReturn(new CityInfoResponse());
        given(metaWeatherExternal.getTemperatureByCity(Mockito.anyInt(),Mockito.any())).willReturn(Mockito.anyDouble());

        this.mockMvc.perform(get("/city/temperature/{cityName}", "santiago"))
                .andExpect(status().isOk());
    }
}
