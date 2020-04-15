package ru.griga.tickets.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ru.griga.tickets.model.itinerary.Itinerary;

import java.io.IOException;

public class SkyscannerQuotaDeserializer extends StdDeserializer<Itinerary[]> {

    public SkyscannerQuotaDeserializer() {
        super(Itinerary[].class);
    }

    @Override
    public Itinerary[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var tree = p.getCodec().readTree(p);

        var quotes = (ArrayNode) tree.get("Quotes");
        var currencies = (ArrayNode) tree.get("Currencies");
        var carriers = (ArrayNode) tree.get("Carriers");
        var places = (ArrayNode) tree.get("Places");

        return new Itinerary[quotes.size()];

    }

}
