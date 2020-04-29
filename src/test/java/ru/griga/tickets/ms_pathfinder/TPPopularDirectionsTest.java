package ru.griga.tickets.ms_pathfinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.griga.tickets.ms_pathfinder.service.TravelPayoutsPopularDirectionsService;

import java.io.IOException;
import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:ms_gatherer.properties")
@SpringBootTest
public class TPPopularDirectionsTest {

    @Autowired
    private TravelPayoutsPopularDirectionsService travelPayoutsPopularDirectionsService;

    @Test
    public void testPopularDirectionsFetching() throws IOException {
        var result = travelPayoutsPopularDirectionsService.getPopularDirectionsFromPlace("KGD");

        assert result.size() == 10;
        assert result.keySet().stream().allMatch((it) -> it.length() == 3);
        assert result.values().stream().allMatch((it) -> it.compareTo(BigDecimal.ZERO) > 0);
    }

}
