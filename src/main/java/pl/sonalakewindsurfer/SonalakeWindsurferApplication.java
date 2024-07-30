package pl.sonalakewindsurfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.sonalakewindsurfer.config.LocationsConfig;

@SpringBootApplication
@EnableConfigurationProperties(LocationsConfig.class)
public class SonalakeWindsurferApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonalakeWindsurferApplication.class, args);
    }

}
