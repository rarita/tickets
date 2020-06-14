package ru.griga.tickets.shared.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.griga.tickets.shared.model.Carrier;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.itinerary.SkyPickerItinerary;
import ru.griga.tickets.shared.model.place.base.Place;
import ru.griga.tickets.shared.model.serialization.SkyPickerDataDeserializer;
import ru.griga.tickets.shared.model.serialization.SkyPickerItinerarySerializer;
import ru.griga.tickets.shared.model.serialization.SkyscannerQuotaDeserializer;
import ru.griga.tickets.shared.model.serialization.travel_payouts.TravelPayoutsCarrierDeserializer;
import ru.griga.tickets.shared.model.serialization.travel_payouts.TravelPayoutsGeoDataDeserializer;
import ru.griga.tickets.shared.repository.CarrierRepository;
import ru.griga.tickets.shared.repository.TravelRepository;

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
    StdSerializer<SkyPickerItinerary> provideSkyPickerItinerarySerializer() {
        return new SkyPickerItinerarySerializer();
    }

    @Bean
    @Qualifier("deser-module")
    public SimpleModule provideDeserializationModule(StdDeserializer<Itinerary[]> quotaDeserializer,
                                                     StdDeserializer<Place> geoDataDeserializer,
                                                     StdDeserializer<Carrier> carrierDeserializer,
                                                     StdDeserializer<Itinerary> skyPickerDeserializer,
                                                     StdSerializer<SkyPickerItinerary> skyPickerItinerarySerializer) {
        return new SimpleModule()
                //.addSerializer(SkyPickerItinerary.class, skyPickerItinerarySerializer)
                .addDeserializer(Itinerary[].class, quotaDeserializer)
                .addDeserializer(Place.class, geoDataDeserializer)
                .addDeserializer(Carrier.class, carrierDeserializer)
                .addDeserializer(Itinerary.class, skyPickerDeserializer);
    }

    @Bean
    public ObjectMapper provideObjectMapper(@Qualifier("deser-module") SimpleModule module) {

        var om = new ObjectMapper()
                    .registerModule(module)
                    .registerModule(new JavaTimeModule());

        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return om;

    }

}
