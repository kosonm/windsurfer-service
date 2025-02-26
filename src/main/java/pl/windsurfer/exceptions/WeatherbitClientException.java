package pl.windsurfer.exceptions;

import lombok.Getter;

@Getter
public class WeatherbitClientException extends Exception {

    public final int statusCodeValue;

    public WeatherbitClientException(String message, int statusCodeValue) {
        super(message);
        this.statusCodeValue = statusCodeValue;
    }

}
