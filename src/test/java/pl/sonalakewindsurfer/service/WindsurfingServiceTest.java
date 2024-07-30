package pl.sonalakewindsurfer.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sonalakewindsurfer.config.LocationsConfig;
import pl.sonalakewindsurfer.exceptions.WeatherbitClientException;
import pl.sonalakewindsurfer.model.Location;
import pl.sonalakewindsurfer.model.WeatherData;
import pl.sonalakewindsurfer.model.WeatherResponse;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WindsurfingServiceTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private LocationsConfig locationsConfig;

    @InjectMocks
    private WindsurfingService windsurfingService;

    private List<Location> locations;
    private Location location1;
    private Location location2;

    @BeforeEach
    void setUp() {
        location1 = Location.builder().latitude(50.0).longitude(19.0).name("Location1").build();
        location2 = Location.builder().latitude(51.0).longitude(20.0).name("Location2").build();
        locations = Arrays.asList(location1, location2);
    }

    @Test
    void testGetBestWindsurfingLocationSuccess() throws WeatherbitClientException {
        // given
        WeatherData weatherData = WeatherData.builder()
                .date(LocalDate.of(2023, 7, 1))
                .temperature(25.0)
                .windSpeed(10.0)
                .build();

        when(locationsConfig.getLocations()).thenReturn(locations);
        when(weatherService.getWeatherData(location1)).thenReturn(Collections.singletonList(weatherData));
        when(weatherService.getWeatherData(location2)).thenReturn(Collections.singletonList(weatherData));


        // when
        Optional<WeatherResponse> response = windsurfingService.getBestWindsurfingLocation(LocalDate.of(2023, 7, 1));

        // then
        assertThat(response).isPresent();
        assertThat(response.get().getLocation()).isEqualTo("Location1");
        assertThat(response.get().getTemperature()).isEqualTo(25.0);
        assertThat(response.get().getWindSpeed()).isEqualTo(10.0);
    }

    @Test
    void testGetBestWindsurfingLocationMultipleLocations() throws WeatherbitClientException {
        // given
        WeatherData weatherData1 = WeatherData.builder()
                .date(LocalDate.of(2023, 7, 1))
                .temperature(25.0)
                .windSpeed(10.0)
                .build();

        WeatherData weatherData2 = WeatherData.builder()
                .date(LocalDate.of(2023, 7, 1))
                .temperature(30.0)
                .windSpeed(15.0)
                .build();

        when(locationsConfig.getLocations()).thenReturn(locations);
        when(weatherService.getWeatherData(location1)).thenReturn(Collections.singletonList(weatherData1));
        when(weatherService.getWeatherData(location2)).thenReturn(Collections.singletonList(weatherData2));

        // when
        Optional<WeatherResponse> response = windsurfingService.getBestWindsurfingLocation(LocalDate.of(2023, 7, 1));

        // then
        assertThat(response).isPresent();
        assertThat(response.get().getLocation()).isEqualTo("Location2");
        assertThat(response.get().getTemperature()).isEqualTo(30.0);
        assertThat(response.get().getWindSpeed()).isEqualTo(15.0);
    }

    @Test
    void testGetBestWindsurfingLocationNoSuitableConditions() throws WeatherbitClientException {
        // given
        WeatherData weatherData = WeatherData.builder()
                .date(LocalDate.of(2023, 7, 1))
                .temperature(40.0)
                .windSpeed(2.0)
                .build();

        when(locationsConfig.getLocations()).thenReturn(locations);
        when(weatherService.getWeatherData(location1)).thenReturn(Collections.singletonList(weatherData));

        // when
        Optional<WeatherResponse> response = windsurfingService.getBestWindsurfingLocation(LocalDate.of(2023, 7, 1));

        // then
        assertThat(response).isNotPresent();
    }

    @Test
    void testGetBestWindsurfingLocationWeatherbitClientException() throws WeatherbitClientException {
        // given
        when(locationsConfig.getLocations()).thenReturn(locations);
        when(weatherService.getWeatherData(location1)).thenThrow(new WeatherbitClientException("Service unavailable", 500));

        // when
        WeatherbitClientException exception = assertThrows(WeatherbitClientException.class, () -> {
            windsurfingService.getBestWindsurfingLocation(LocalDate.of(2023, 7, 1));
        });

        // then
        assertThat(exception.getMessage()).contains("Service unavailable");
    }
}
