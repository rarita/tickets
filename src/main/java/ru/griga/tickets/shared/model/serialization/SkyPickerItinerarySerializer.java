package ru.griga.tickets.shared.model.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.itinerary.SkyPickerItinerary;

import java.io.IOException;

public class SkyPickerItinerarySerializer extends StdSerializer<SkyPickerItinerary> {

    public SkyPickerItinerarySerializer() {
        super(SkyPickerItinerary.class);
    }

    @Override
    public void serialize(SkyPickerItinerary v,
                          JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("SERIAL_UID", v.SERIAL_UID);
        jgen.writeNumberField("id", v.getId());
        jgen.writeObjectField("source", v.getSource());
        jgen.writeObjectField("destination", v.getDestination());
        jgen.writeNumberField("cost", v.getCost());
        jgen.writeNumberField("baseCost", v.getBaseCost());
        jgen.writeStringField("currency", v.getCurrencyCode());
        jgen.writeStringField("carrierCode", v.getCarrierCode());
        jgen.writeObjectField("departureTime", v.getDepartureTime());
        jgen.writeObjectField("arrivalTime", v.getArrivalTime());
        jgen.writeObjectField("foundAt", v.getFoundAt());
        jgen.writeNumberField("ttl", v.getTtl());
        //jgen.writeObjectField("type", v.);

        jgen.writeEndObject();

    }
}
