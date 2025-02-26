package pl.windsurfer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WeatherResponse {
    private String location;
    private double temperature;
    private double windSpeed;
}