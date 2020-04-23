package ru.griga.tickets.ms_pathfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
        "ru.griga.tickets.ms_pathfinder",
        "ru.griga.tickets.shared"})
@EntityScan("ru.griga.tickets.shared.model")
@EnableNeo4jRepositories("ru.griga.tickets.shared.repository")
@EnableDiscoveryClient
public class PathFinderApplication {

    /**
     * В базе все таймштампы берутся от UTC+0
     * Все времена из ответов SkyPicker тоже в этом поясе
     * Значит, и JVM быть!
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "ms_pathfinder");
        SpringApplication.run(ru.griga.tickets.ms_gatherer.TicketsGathererApplication.class, args);
    }

}