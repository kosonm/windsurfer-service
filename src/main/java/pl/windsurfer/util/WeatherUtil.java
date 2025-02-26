package pl.sonalakewindsurfer.util;

import pl.sonalakewindsurfer.model.WeatherData;

public class WeatherUtil {

    public static boolean isSuitableForWindsurfing(WeatherData data) {
        double windSpeed = data.getWindSpeed();
        double temperature = data.getTemperature();
        return (windSpeed >= 5 && windSpeed <= 18) && (temperature >= 5 && temperature <= 35);
    }

    public static double calculateScore(WeatherData data) {
        return data.getWindSpeed() * 3 + data.getTemperature();
    }
}
