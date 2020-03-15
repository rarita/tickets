package ru.griga.tickets;

import org.junit.Test;
import ru.griga.tickets.model.SearchParams;

import java.time.LocalDate;

public class SearchParamsTest {

    @Test
    public void testToBody() {
        String expected = "country=US&currency=USD&locale=en-US&originPlace=SFO-sky&destinationPlace=LHR-sky&outboundDate=2019-02-14&adults=1";

        SearchParams searchParams = new SearchParams("US",
                "USD",
                "en-US",
                "SFO-sky",
                "LHR-sky",
                LocalDate.of(2019, 2, 14),
                1);

        //assert searchParams.toRequestBody().equals(expected);
    }

}
