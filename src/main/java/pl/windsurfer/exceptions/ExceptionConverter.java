package pl.windsurfer.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionConverter {

    @ExceptionHandler(WeatherbitClientException.class)
    public ResponseEntity<String> handleWeatherbitClientException(WeatherbitClientException e) {
        return ResponseEntity.status(e.getStatusCodeValue()).body(e.getMessage());
    }
}
