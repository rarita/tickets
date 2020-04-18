package ru.griga.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class TicketsApplication {

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
        SpringApplication.run(TicketsApplication.class, args);
    }

}
