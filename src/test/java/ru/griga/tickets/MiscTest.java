package ru.griga.tickets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ru.griga.tickets.shared.model.ItineraryType;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.model.itinerary.SkyPickerItinerary;

import java.time.LocalDate;
import java.util.List;

public class MiscTest {

    private final LocalDate targetDate = LocalDate.now();

    private final SearchParams searchParams = new SearchParams(
            "RU",
            "RUB",
            "ru-RU",
            "LED",
            "KGD",
            LocalDate.of(2020, 4, 17),
            LocalDate.of(2020, 4, 17), //.plusDays(1),
            1, 0, 0,
            List.of(ItineraryType.AIRCRAFT, ItineraryType.BUS, ItineraryType.TRAIN));

    private final String searchParamsJSON
            = "{\"countryCode\":\"RU\",\"currencyCode\":\"RUB\",\"locale\":\"ru-RU\",\"originCode\":\"LED\",\"destinationCode\":\"KGD\",\"outboundDateFrom\":[2020,4,17],\"outboundDateTo\":[2020,4,17],\"adultsCount\":1,\"childrenCount\":0,\"infantsCount\":0,\"typesAllowed\":[\"AIRCRAFT\",\"BUS\",\"TRAIN\"]}";

    @Test
    public void testItinerariesHashCodeEquality() {
        var x = new SkyPickerItinerary();
        var y = new SkyPickerItinerary();

        assert x.equals(y);
        assert x.hashCode() == y.hashCode();
    }

    @Test
    public void testJSONMapping() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(searchParams);

        assert response.equals(searchParamsJSON);
    }

}
