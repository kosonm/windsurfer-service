package pl.windsurfer.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import pl.windsurfer.model.WeatherData;

import java.util.stream.Stream;

class WeatherUtilTest {

    @ParameterizedTest
    @CsvSource({
            "10, 20, true",   // suitable conditions
            "4, 20, false",   // wind speed too low
            "19, 20, false",  // wind speed too high
            "10, 4, false",   // temperature too low
            "10, 36, false"   // temperature too high
    })
    void testIsSuitableForWindsurfing(double windSpeed, double temperature, boolean expected) {
        WeatherData data = WeatherData.builder()
                .windSpeed(windSpeed)
                .temperature(temperature)
                .build();
        assertThat(WeatherUtil.isSuitableForWindsurfing(data)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideWeatherDataForCalculateScore")
    void testCalculateScore(WeatherData data, double expectedScore) {
        assertThat(WeatherUtil.calculateScore(data)).isEqualTo(expectedScore);
    }

    private static Stream<Object[]> provideWeatherDataForCalculateScore() {
        return Stream.of(
                new Object[]{WeatherData.builder().windSpeed(10).temperature(20).build(), 50},
                new Object[]{WeatherData.builder().windSpeed(5).temperature(15).build(), 30},
                new Object[]{WeatherData.builder().windSpeed(18).temperature(35).build(), 89}
        );
    }
}
