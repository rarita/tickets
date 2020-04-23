package ru.griga.tickets.ms_gatherer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import kong.unirest.UnirestException;
import kong.unirest.UnirestInstance;
import org.junit.Test;
import ru.griga.tickets.shared.configuration.UnirestConfig;
import ru.griga.tickets.shared.model.ItineraryType;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.model.serialization.SkyscannerQuotaDeserializer;
import ru.griga.tickets.ms_gatherer.service.BaseDataService;
import ru.griga.tickets.ms_gatherer.service.LiveSkyscannerDataService;
import ru.griga.tickets.ms_gatherer.service.QuotaSkyscannerDataService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class SkyscannerDataServiceTest {

    private final SearchParams searchParams = new SearchParams(
            "RU",
            "RUB",
            "ru-RU",
            "LED-sky",
            "KGD-sky",
            LocalDate.now(),
            LocalDate.now().plusDays(1),
            1, 0, 0,
            List.of(ItineraryType.AIRCRAFT));

    private final UnirestInstance unirestInstance =
        new UnirestConfig().skyScannerUnirestInstance();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new SimpleModule().addDeserializer(Itinerary[].class, new SkyscannerQuotaDeserializer ()));

    private final BaseDataService liveSDS =
        new LiveSkyscannerDataService(unirestInstance);

    private final BaseDataService quotaSDS =
        new QuotaSkyscannerDataService(unirestInstance, objectMapper);

    @Test
    public void testLiveRequest() throws UnirestException, IOException {
         liveSDS.fetchData(searchParams);
    }

    @Test
    public void testQuotaSearch() throws UnirestException, IOException {
            Itinerary[] result = (Itinerary[]) quotaSDS.fetchData(searchParams);
            for (Itinerary item : result)
                System.out.println(item.toString());
    }

}
