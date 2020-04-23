package ru.griga.tickets.ms_gatherer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.griga.tickets.shared.model.ItineraryType;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.repository.ItineraryRepository;
import ru.griga.tickets.shared.repository.TravelRepository;
import ru.griga.tickets.ms_gatherer.service.SkyPickerDataService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Этому тесту нужны рантайм-бины репозиториев
 * Поэтому я не хочу его класть в класс с тестами геодаты с TravelPayouts
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkyPickerDataServiceTest {

    private final LocalDate targetDate = LocalDate.now().plusDays(5);

    private final SearchParams searchParams = new SearchParams(
            "RU",
            "RUB",
            "ru-RU",
            "LED",
            "KGD",
            targetDate,
            targetDate, //.plusDays(1),
            1, 0, 0,
            List.of(ItineraryType.AIRCRAFT, ItineraryType.BUS, ItineraryType.TRAIN));

    @Autowired
    private SkyPickerDataService spds;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Test
    public void skyPickerBasicTest() throws IOException {

        var result = spds.persistData(searchParams);
        System.out.println(result);

    }

}
