package pl.sonalakewindsurfer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.sonalakewindsurfer.exceptions.WeatherbitClientException;
import pl.sonalakewindsurfer.model.WeatherResponse;
import pl.sonalakewindsurfer.service.WindsurfingService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WindsurfingService windsurfingService;

    @GetMapping("/api/weather/best-location")
    public ResponseEntity<WeatherResponse> getBestWindsurfingLocation(@RequestParam("date") String date) throws WeatherbitClientException {
        LocalDate targetDate = LocalDate.parse(date);
        return windsurfingService.getBestWindsurfingLocation(targetDate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
