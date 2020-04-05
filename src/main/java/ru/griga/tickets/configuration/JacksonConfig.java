package ru.griga.tickets.configuration;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import ru.griga.tickets.model.Itinerary;
import ru.griga.tickets.model.serialization.SkyscannerQuotaDeserializer;

public class JacksonConfig {

    @Bean
    public StdDeserializer<Itinerary[]> provideQuotaDeserializer() {
        return new SkyscannerQuotaDeserializer();
    }

    @Bean
    public SimpleModule provideDeserializationModule(StdDeserializer<Itinerary[]> quotaDeserializer) {
        return new SimpleModule()
                .addDeserializer(Itinerary[].class, quotaDeserializer);
    }

    @Bean
    public ObjectMapper provideObjectMapper(SimpleModule module) {
        return new ObjectMapper()
                .registerModule(module);
    }

}
