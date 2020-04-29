package ru.griga.tickets.shared.model.place;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Relationship;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.place.base.GeoPlace;
import ru.griga.tickets.shared.model.place.base.Place;

import java.util.Set;

public class Airport extends GeoPlace {

    @Getter
    @Setter
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "CAN_GO")
    private Set<Itinerary> itineraries;

    public Airport() {
    }

    public Airport(String code, String name_RU, String name_EN, double latitude, double longitude, Place parent) {
        super(code, name_RU, name_EN, latitude, longitude, parent);
    }

}
