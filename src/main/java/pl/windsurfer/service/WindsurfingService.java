package pl.sonalakewindsurfer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.sonalakewindsurfer.config.LocationsConfig;
import pl.sonalakewindsurfer.exceptions.WeatherbitClientException;
import pl.sonalakewindsurfer.model.Location;
import pl.sonalakewindsurfer.model.WeatherData;
import pl.sonalakewindsurfer.model.WeatherResponse;
import pl.sonalakewindsurfer.util.WeatherUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WindsurfingService {

    private final WeatherService weatherService;
    private final LocationsConfig locationsConfig;

    public Optional<WeatherResponse> getBestWindsurfingLocation(LocalDate targetDate) throws WeatherbitClientException {
        List<Location> locations = locationsConfig.getLocations();
        List<WeatherResponse> weatherResponses = new ArrayList<>();

        for (Location location : locations) {
            weatherResponses.addAll(getWeatherResponsesForLocation(location, targetDate));
        }

        return weatherResponses.stream()
                .max(Comparator.comparingDouble(this::calculateScore));
    }

    private List<WeatherResponse> getWeatherResponsesForLocation(Location location, LocalDate targetDate) throws WeatherbitClientException {
        List<WeatherData> weatherDataList = weatherService.getWeatherData(location);
        List<WeatherResponse> weatherResponses = new ArrayList<>();

        weatherDataList.stream()
                .filter(data -> data.getDate().equals(targetDate))
                .filter(WeatherUtil::isSuitableForWindsurfing)
                .map(data -> mapToWeatherResponse(location, data))
                .forEach(weatherResponses::add);

        return weatherResponses;
    }

    private WeatherResponse mapToWeatherResponse(Location location, WeatherData data) {
        return WeatherResponse.builder()
                .location(location.getName())
                .temperature(data.getTemperature())
                .windSpeed(data.getWindSpeed())
                .build();
    }

    private double calculateScore(WeatherResponse response) {
        return WeatherUtil.calculateScore(WeatherData.builder()
                .temperature(response.getTemperature())
                .windSpeed(response.getWindSpeed())
                .build());
    }
}

