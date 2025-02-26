package pl.windsurfer.model;

import lombok.*;


@Getter
@Setter
@Builder
public class Location {
    private String name;
    private double latitude;
    private double longitude;
}
