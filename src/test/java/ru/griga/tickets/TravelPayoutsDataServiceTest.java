package ru.griga.tickets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ru.griga.tickets.configuration.JacksonConfig;
import ru.griga.tickets.configuration.UnirestConfig;
import ru.griga.tickets.service.TravelPayoutsGeoService;

import java.io.IOException;

public class TravelPayoutsDataServiceTest {

    private final JacksonConfig jacksonConfig = new JacksonConfig();

    private final ObjectMapper objectMapper = jacksonConfig
            .provideObjectMapper(jacksonConfig.provideDeserializationModule(jacksonConfig.provideQuotaDeserializer(), jacksonConfig.provideGeoDataDeserializer()));

    private final TravelPayoutsGeoService tpds
            = new TravelPayoutsGeoService(new UnirestConfig().travelPayoutsUnirestInstance(), objectMapper, null);

    @Test
    public void requestLocations() throws IOException {
        var locations = tpds.getLocations();
        assert locations.size() > 5000;
    }

}
