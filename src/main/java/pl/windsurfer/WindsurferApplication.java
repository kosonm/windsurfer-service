package pl.windsurfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.windsurfer.config.LocationsConfig;

@SpringBootApplication
@EnableConfigurationProperties(LocationsConfig.class)
public class WindsurferApplication {

    public static void main(String[] args) {
        SpringApplication.run(WindsurferApplication.class, args);
    }

}
