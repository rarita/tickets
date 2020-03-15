package ru.griga.tickets;

import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import kong.unirest.UnirestInstance;
import org.junit.Test;
import ru.griga.tickets.configuration.UnirestConfig;
import ru.griga.tickets.model.SearchParams;
import ru.griga.tickets.service.BaseSkyscannerDataService;
import ru.griga.tickets.service.LiveSkyscannerDataService;
import ru.griga.tickets.service.QuotaSkyscannerDataService;

import java.time.LocalDate;

public class SkyscannerDataServiceTest {

    private final SearchParams searchParams = new SearchParams(
            "RU",
            "RUB",
            "ru-RU",
            "LED-sky",
            "KGD-sky",
            LocalDate.of(2020, 4, 10),
            1);

    private final UnirestInstance unirestInstance =
        new UnirestConfig().unirestInstance();

    private final BaseSkyscannerDataService liveSDS =
        new LiveSkyscannerDataService(unirestInstance);

    private final BaseSkyscannerDataService quotaSDS =
        new QuotaSkyscannerDataService(unirestInstance);

    @Test
    public void testLiveRequest() throws UnirestException {
         liveSDS.fetchData(searchParams);
    }

    @Test
    public void testQuotaSearch() throws UnirestException {
        HttpResponse<String> result = (HttpResponse<String>) quotaSDS.fetchData(searchParams);
        assert result.isSuccess();
    }

}
