package ru.griga.tickets.service;

import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.griga.tickets.model.place.Airport;

import java.util.List;

public class TravelPayoutsGeoService {

    private final UnirestInstance unirestInstance;

    public TravelPayoutsGeoService(@Autowired @Qualifier("travel-payouts") UnirestInstance unirestInstance) {
        this.unirestInstance = unirestInstance;
    }

    public List<Airport> getAllAirports() {
        return null;
    }



}
