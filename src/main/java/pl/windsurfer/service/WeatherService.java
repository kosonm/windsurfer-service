package pl.sonalakewindsurfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.sonalakewindsurfer.exceptions.WeatherbitClientException;
import pl.sonalakewindsurfer.model.Location;
import pl.sonalakewindsurfer.model.WeatherApiResponse;
import pl.sonalakewindsurfer.model.WeatherData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    @Value("${weatherbit.api.key}")
    private String apiKey;

    @Value("${weatherbit.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<WeatherData> getWeatherData(Location location) throws WeatherbitClientException {
        String url = String.format("%s?lat=%f&lon=%f&key=%s", apiUrl, location.getLatitude(), location.getLongitude(), apiKey);
        ResponseEntity<WeatherApiResponse> response;

        try {
            response = restTemplate.getForEntity(url, WeatherApiResponse.class);
        } catch (RestClientException e) {
            String msg = String.format("[WeatherService] Request to Weatherbit service for location: %s returned error message: %s", location, e.getMessage());
            log.error(msg);
            throw new WeatherbitClientException(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            String msg = String.format("[WeatherService] Request to Weatherbit service for location: %s returned code %s message: %s", location, response.getStatusCode(), response);
            log.error(msg);
            throw new WeatherbitClientException(msg, response.getStatusCode().value());
        }

        if (response.getBody() == null) {
            String msg = String.format("[WeatherService] Request to Weatherbit service for location: %s returned null body.", location);
            log.error(msg);
            throw new WeatherbitClientException(msg, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return response.getBody().getData().stream()
                .map(entry -> WeatherData.builder()
                .date(LocalDate.parse(entry.getValidDate(), dateFormatter))
                .temperature(entry.getTemp())
                .windSpeed(entry.getWindSpd())
                .build())
                .toList();

    }

}
