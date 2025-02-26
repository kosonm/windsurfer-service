package pl.windsurfer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.windsurfer.exceptions.WeatherbitClientException;
import pl.windsurfer.model.Location;
import pl.windsurfer.model.WeatherApiResponse;
import pl.windsurfer.model.WeatherData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @Value("${weatherbit.api.key}")
    private String apiKey = "dummyApiKey";

    @Value("${weatherbit.api.url}")
    private String apiUrl = "http://dummyurl.com";

    @Test
    void testGetWeatherDataSuccess() throws WeatherbitClientException {
        // given
        Location location = Location.builder().latitude(50.0).longitude(19.0).build();
        WeatherApiResponse.WeatherDataEntry data = WeatherApiResponse.WeatherDataEntry.builder()
                .validDate("2023-07-01")
                .temp(25.0)
                .windSpd(10.0)
                .build();
        WeatherApiResponse mockResponse = WeatherApiResponse.builder()
                .cityName("Dummy City")
                .data(Collections.singletonList(data))
                .build();

        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // when
        List<WeatherData> weatherData = weatherService.getWeatherData(location);

        // then
        assertThat(weatherData).hasSize(1);
        assertThat(weatherData.get(0).getDate()).isEqualTo(LocalDate.of(2023, 7, 1));
        assertThat(weatherData.get(0).getTemperature()).isEqualTo(25.0);
        assertThat(weatherData.get(0).getWindSpeed()).isEqualTo(10.0);
    }

    @Test
    void testGetWeatherDataRestClientException() {
        // given
        Location location = Location.builder().latitude(50.0).longitude(19.0).build();
        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponse.class)))
                .thenThrow(new RestClientException("Service unavailable"));

        // when
        WeatherbitClientException exception = assertThrows(WeatherbitClientException.class, () -> {
            weatherService.getWeatherData(location);
        });

        // then
        assertThat(exception.getMessage()).contains("Request to Weatherbit service for location");
    }

    @Test
    void testGetWeatherDataNon2xxResponse() {
        // given
        Location location = Location.builder().latitude(50.0).longitude(19.0).build();
        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // when
        WeatherbitClientException exception = assertThrows(WeatherbitClientException.class, () -> {
            weatherService.getWeatherData(location);
        });

        // then
        assertThat(exception.getMessage()).contains("returned code 400");
    }

    @Test
    void testGetWeatherDataNullBody() {
        // given
        Location location = Location.builder().latitude(50.0).longitude(19.0).build();
        when(restTemplate.getForEntity(anyString(), eq(WeatherApiResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // when
        WeatherbitClientException exception = assertThrows(WeatherbitClientException.class, () -> {
            weatherService.getWeatherData(location);
        });

        // then
        assertThat(exception.getMessage()).contains("returned null body");
    }
}
