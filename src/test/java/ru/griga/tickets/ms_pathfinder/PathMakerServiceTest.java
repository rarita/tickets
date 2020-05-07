package ru.griga.tickets.ms_pathfinder;

import org.assertj.core.internal.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.griga.tickets.ms_pathfinder.service.PathDiscoveryService;
import ru.griga.tickets.ms_pathfinder.service.PathMakerService;
import ru.griga.tickets.shared.model.ItineraryType;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.repository.ItineraryRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:ms_gatherer.properties")
@SpringBootTest
public class PathMakerServiceTest {

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
    private PathDiscoveryService pds;

    @Autowired
    private PathMakerService pms;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Test
    public void testPathDiscovery() throws IOException {
        var res = pds.discoverPathsFrom("AMS", 2);
        assert res.get("status").equals("OK");
    }

    @Test
    public void testPathMaking() throws IOException {
        pms.makePathsFor(searchParams);
    }

    @Test
    public void testItinerariesSearchByQueryParams() {
        var searchParams = this.searchParams.clone();
        searchParams.setDestinationCode("MOW");
        searchParams.setOutboundDateFrom(LocalDate.of(2020, 4, 30));
        //searchParams.setOutboundDateFrom(LocalDate.of(2020, 5, 1));
        searchParams.setOutboundDateTo(LocalDate.of(2020, 5, 1));
        searchParams.setTypesAllowed(Collections.singletonList(ItineraryType.AIRCRAFT));

        var x = itineraryRepository.findItinerariesBySearchParams(searchParams);

        System.out.println("wow");
    }

    @Test
    public void testOutputOfTestQuery() {
        var searchParams = this.searchParams.clone();
        searchParams.setOutboundDateFrom(LocalDate.of(2020, 4, 30));
        var x = itineraryRepository.testQuery(searchParams);

        System.out.println("test completed!");
    }

}