package ru.griga.tickets.model.place;

import ru.griga.tickets.model.Itinerary;
import ru.griga.tickets.model.place.base.Place;

import java.util.Set;

public class Country extends Place {

    public Country() {
    }

    public Country(String code, String name_RU, String name_EN, Place parent) {
        super(code, name_RU, name_EN, parent);
    }

}
