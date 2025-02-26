package pl.windsurfer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pl.windsurfer.model.Location;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "locations")
public class LocationsConfig {
    private List<Location> locations;
}
