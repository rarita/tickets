package ru.griga.tickets.configuration;

import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnirestConfig {

    private static final String RAPID_API_SC_HOST = "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    private static final String RAPID_API_SC_KEY = "8f6225f93amsh28cb7e78558bfa3p135635jsna011d74f4fd0";

    private static final String RAPID_API_TPAY_HOST = "travelpayouts-travelpayouts-flight-data-v1.p.rapidapi.com";
    private static final String TPAY_ACCESS_TOKEN = "ec37b767477b0a047511f2d4bffb5108";

    @Bean
    @Qualifier("clean")
    public UnirestInstance getCleanUnirestInstance() {
        var instance = Unirest.spawnInstance();
        // Accept gzip
        instance.config().addDefaultHeader("accept-encoding", "gzip, deflate");

        return instance;
    }

    @Bean
    @Qualifier("skyscanner")
    public UnirestInstance skyScannerUnirestInstance() {
        var instance = Unirest.spawnInstance();
        var config = instance.config();

        config.addDefaultHeader("x-rapidapi-host", RAPID_API_SC_HOST);
        config.addDefaultHeader("x-rapidapi-key", RAPID_API_SC_KEY);
        config.addDefaultHeader("content-type", "application/x-www-form-urlencoded");

        return instance;
    }

    @Bean
    @Qualifier("travel-payouts")
    public UnirestInstance travelPayoutsUnirestInstance() {
        var instance = Unirest.spawnInstance();
        var config = instance.config();

        config.addDefaultHeader("x-rapidapi-host", RAPID_API_TPAY_HOST);
        config.addDefaultHeader("x-rapidapi-key", RAPID_API_SC_KEY);
        config.addDefaultHeader("x-access-token", TPAY_ACCESS_TOKEN);

        // Accept gzip
        config.addDefaultHeader("accept-encoding", "gzip, deflate");
        config.addDefaultHeader("content-type", "application/x-www-form-urlencoded");

        return instance;
    }

}
