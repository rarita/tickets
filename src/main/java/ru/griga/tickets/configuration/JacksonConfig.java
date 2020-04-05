package ru.griga.tickets.configuration;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.griga.tickets.model.Itinerary;
import ru.griga.tickets.model.place.base.Place;
import ru.griga.tickets.model.serialization.SkyscannerQuotaDeserializer;
import ru.griga.tickets.model.serialization.TravelPayoutsGeoDataDeserializer;

@Configuration
public class JacksonConfig {

    @Bean
    public StdDeserializer<Itinerary[]> provideQuotaDeserializer() {
        return new SkyscannerQuotaDeserializer();
    }

    @Bean
    public StdDeserializer<Place> provideGeoDataDeserializer() {
        return new TravelPayoutsGeoDataDeserializer();
    }

    @Bean
    @Qualifier("deser-module")
    public SimpleModule provideDeserializationModule(StdDeserializer<Itinerary[]> quotaDeserializer,
                                                     StdDeserializer<Place> geoDataDeserializer) {
        return new SimpleModule()
                .addDeserializer(Itinerary[].class, quotaDeserializer)
                .addDeserializer(Place.class, new TravelPayoutsGeoDataDeserializer());
    }

    @Bean
    public ObjectMapper provideObjectMapper(@Qualifier("deser-module") SimpleModule module) {
        return new ObjectMapper()
                .registerModule(module);
    }

}
