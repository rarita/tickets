package ru.griga.tickets;

import ru.griga.tickets.configuration.UnirestConfig;
import ru.griga.tickets.service.TravelPayoutsGeoService;

public class TravelPayoutsDataServiceTest {

    private final UnirestConfig unirestConfig = new UnirestConfig();

    private final TravelPayoutsGeoService tpds
            = new TravelPayoutsGeoService(new UnirestConfig().travelPayoutsUnirestInstance());

    public void requestLocations() {

    }

}
