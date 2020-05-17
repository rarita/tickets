package ru.griga.tickets.shared.model.place.base;

import lombok.*;
import ru.griga.tickets.shared.model.place.Airport;

/**
 * Класс Place с известным местоположением
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GeoPlace extends Place {

    @Setter(AccessLevel.NONE)
    private double latitude;

    @Setter(AccessLevel.NONE)
    private double longitude;

    public GeoPlace(String code, String name_RU, String name_EN, double latitude, double longitude, Place parent) {

        super(code, name_RU, name_EN, parent);
        this.latitude = latitude;
        this.longitude = longitude;

    }

}
