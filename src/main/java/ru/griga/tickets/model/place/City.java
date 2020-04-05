package ru.griga.tickets.model.place;

import ru.griga.tickets.model.place.base.GeoPlace;
import ru.griga.tickets.model.place.base.Place;

public class City extends GeoPlace {

    public City() {
    }

    public City(String code, String name_RU, String name_EN, double latitude, double longitude, Place parent) {
        super(code, name_RU, name_EN, latitude, longitude, parent);
    }

}
