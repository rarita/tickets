package ru.griga.tickets.ms_pathfinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:ms_gatherer.properties")
@SpringBootTest
public class TPPopularDirectionsTest {

    @Autowired
    private PathDiscoveryService pathDiscoveryService;

    @Test
    public void testPopularDirectionsFetching() throws IOException {
        var result = pathDiscoveryService.getPopularDirectionsFromPlace("KGD");

        assert result.size() == 10;
        assert result.stream().allMatch((it) -> it.length() == 3);

    }

}
