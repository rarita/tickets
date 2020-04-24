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
public class PathDiscoveryServiceTest {

    @Autowired
    private PathDiscoveryService pds;

    @Test
    public void testPathDiscovery() throws IOException {
        var res = pds.discoverPathsFrom("LED", 2);
        assert res.get("status").equals("OK");
    }

}
