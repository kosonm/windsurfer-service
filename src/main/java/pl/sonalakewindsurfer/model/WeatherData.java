package pl.sonalakewindsurfer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class WeatherData {
    private double temperature;
    private double windSpeed;
    private LocalDate date;
}
