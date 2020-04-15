package ru.griga.tickets.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.griga.tickets.model.Carrier;
import ru.griga.tickets.model.itinerary.Itinerary;
import ru.griga.tickets.model.place.base.Place;
import ru.griga.tickets.model.serialization.SkyPickerDataDeserializer;
import ru.griga.tickets.model.serialization.SkyscannerQuotaDeserializer;
import ru.griga.tickets.model.serialization.travel_payouts.TravelPayoutsCarrierDeserializer;
import ru.griga.tickets.model.serialization.travel_payouts.TravelPayoutsGeoDataDeserializer;
import ru.griga.tickets.repository.CarrierRepository;
import ru.griga.tickets.repository.TravelRepository;

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
    public StdDeserializer<Carrier> provideCarrierDeserializer() { return new TravelPayoutsCarrierDeserializer(); }

    @Bean
    public StdDeserializer<Itinerary> provideSkyPickerDeserializer(TravelRepository travelRepository,
                                                                   CarrierRepository carrierRepository) {
        return new SkyPickerDataDeserializer(travelRepository, carrierRepository);
    }

    @Bean
    @Qualifier("deser-module")
    public SimpleModule provideDeserializationModule(StdDeserializer<Itinerary[]> quotaDeserializer,
                                                     StdDeserializer<Place> geoDataDeserializer,
                                                     StdDeserializer<Carrier> carrierDeserializer,
                                                     StdDeserializer<Itinerary> skyPickerDeserializer) {
        return new SimpleModule()
                .addDeserializer(Itinerary[].class, quotaDeserializer)
                .addDeserializer(Place.class, geoDataDeserializer)
                .addDeserializer(Carrier.class, carrierDeserializer)
                .addDeserializer(Itinerary.class, skyPickerDeserializer);
    }

    @Bean
    public ObjectMapper provideObjectMapper(@Qualifier("deser-module") SimpleModule module) {
        return new ObjectMapper()
                .registerModule(module);
    }

}
