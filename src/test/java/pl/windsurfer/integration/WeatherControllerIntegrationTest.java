package pl.windsurfer.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.windsurfer.config.LocationsConfig;
import pl.windsurfer.controller.WeatherController;
import pl.windsurfer.exceptions.WeatherbitClientException;
import pl.windsurfer.model.Location;
import pl.windsurfer.model.WeatherData;
import pl.windsurfer.model.WeatherResponse;
import pl.windsurfer.service.WeatherService;
import pl.windsurfer.service.WindsurfingService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WindsurfingService windsurfingService;

    @MockBean
    private LocationsConfig locationsConfig;

    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetBestWindsurfingLocation() throws Exception {
        // given
        Location location = Location.builder().latitude(50.0).longitude(19.0).name("Location1").build();
        WeatherData weatherData = WeatherData.builder()
                .date(LocalDate.of(2023, 7, 1))
                .temperature(25.0)
                .windSpeed(10.0)
                .build();

        WeatherResponse weatherResponse = WeatherResponse.builder()
                .location(location.getName())
                .temperature(weatherData.getTemperature())
                .windSpeed(weatherData.getWindSpeed())
                .build();

        when(locationsConfig.getLocations()).thenReturn(Collections.singletonList(location));
        when(weatherService.getWeatherData(any(Location.class))).thenReturn(Collections.singletonList(weatherData));
        when(windsurfingService.getBestWindsurfingLocation(any(LocalDate.class))).thenReturn(Optional.of(weatherResponse));

        // when & then
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", "2023-07-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"location\":\"Location1\",\"temperature\":25.0,\"windSpeed\":10.0}"));
    }

    @Test
    void testGetBestWindsurfingLocationNoContent() throws Exception {
        // given
        when(windsurfingService.getBestWindsurfingLocation(any(LocalDate.class))).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", "2023-07-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetBestWindsurfingLocationWeatherbitClientException() throws Exception {
        // given
        when(windsurfingService.getBestWindsurfingLocation(any(LocalDate.class))).thenThrow(new WeatherbitClientException("Service unavailable", 500));

        // when & then
        mockMvc.perform(get("/api/weather/best-location")
                        .param("date", "2023-07-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
