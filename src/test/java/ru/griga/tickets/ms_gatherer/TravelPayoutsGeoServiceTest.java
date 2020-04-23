package ru.griga.tickets.ms_gatherer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.griga.tickets.ms_gatherer.service.TravelPayoutsGeoService;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TravelPayoutsGeoServiceTest {

    @Autowired
    private TravelPayoutsGeoService tpgs;

    @Test
    public void requestLocations() throws IOException {
        var locations = tpgs.getLocations();
        assert locations.size() > 5000;
    }


}
