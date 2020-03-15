package ru.griga.tickets.configuration;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnirestConfig {

    private static final String RAPID_API_HOST = "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    private static final String RAPID_API_KEY = "8f6225f93amsh28cb7e78558bfa3p135635jsna011d74f4fd0";

    @Bean
    public UnirestInstance unirestInstance() {
        var instance = Unirest.spawnInstance();
        var config = instance.config();

        config.addDefaultHeader("x-rapidapi-host", RAPID_API_HOST);
        config.addDefaultHeader("x-rapidapi-key", RAPID_API_KEY);
        config.addDefaultHeader("content-type", "application/x-www-form-urlencoded");

        return instance;
    }

}
