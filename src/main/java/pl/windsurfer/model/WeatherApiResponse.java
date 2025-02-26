package pl.sonalakewindsurfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class WeatherApiResponse {
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("data")
    private List<WeatherDataEntry> data;

    @Getter
    @Setter
    @Builder
    public static class WeatherDataEntry {
        @JsonProperty("valid_date")
        private String validDate;

        @JsonProperty("temp")
        private double temp;

        @JsonProperty("wind_spd")
        private double windSpd;
    }
}