package ru.griga.tickets.model.place;

import ru.griga.tickets.model.place.base.GeoPlace;

public class City extends GeoPlace {

    public City() {
    }

    public City(String code, String name_RU, String name_EN, double latitude, double longitude) {
        super(code, name_RU, name_EN, latitude, longitude);
    }

}
