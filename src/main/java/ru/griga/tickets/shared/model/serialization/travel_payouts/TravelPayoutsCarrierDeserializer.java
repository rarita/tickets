package ru.griga.tickets.shared.model.serialization.travel_payouts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.griga.tickets.shared.model.Carrier;

import java.io.IOException;

import static ru.griga.tickets.shared.Utils.getNodeValue;

public class TravelPayoutsCarrierDeserializer extends StdDeserializer<Carrier> {

    public TravelPayoutsCarrierDeserializer() {
        super(Carrier.class);
    }

    @Override
    public Carrier deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        var tree = p.getCodec().readTree(p);

        String code = getNodeValue(tree, "code", null);
        TreeNode names = tree.get("name_translations");
        String name = getNodeValue(names, "en", null);

        return new Carrier(code, name, null);

    }

}
