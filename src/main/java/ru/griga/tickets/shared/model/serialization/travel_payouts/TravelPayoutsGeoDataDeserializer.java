package ru.griga.tickets.shared.model.serialization.travel_payouts;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import ru.griga.tickets.shared.model.place.Airport;
import ru.griga.tickets.shared.model.place.City;
import ru.griga.tickets.shared.model.place.Country;
import ru.griga.tickets.shared.model.place.base.Place;

import java.io.IOException;
import java.util.List;

import static ru.griga.tickets.shared.Utils.getNodeValue;

public class TravelPayoutsGeoDataDeserializer extends StdDeserializer<Place> {

    public TravelPayoutsGeoDataDeserializer() {
        super(Place.class);
    }

    private Place findParent(String code, List<Place> parentsList) {
        for (Place parent : parentsList) {
            if (parent.getCode().equals(code))
                return parent;
        }
        return null;
    }

    @Override
    public Place deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var tree = p.getCodec().readTree(p);

        Class type = (Class) ctxt.findInjectableValue("class", null, null);
        List<Place> parentsList = (List<Place>) ctxt.findInjectableValue("parentsList", null, null);
        String parentNodeName = (String) ctxt.findInjectableValue("parentNodeName", null, null);

        String code = ((TextNode) tree.get("code")).asText();

        String name_RU = getNodeValue(tree, "name", null);

        TreeNode translations = tree.get("name_translations");
        String name_EN = getNodeValue(translations, "en", null);

        Double latitude = null;
        Double longitude = null;

        TreeNode coordinates = tree.get("coordinates");
        if (coordinates != null) {
            latitude = Double.parseDouble(getNodeValue(coordinates, "lat", "0.0"));
            longitude = Double.parseDouble(getNodeValue(coordinates, "lon", "0.0"));
        }

        String parentCode = null;

        if (parentNodeName != null)
            parentCode = getNodeValue(tree, parentNodeName, null);

        if (type.equals(Country.class))
            return new Country(code, name_RU, name_EN, null);
        if (type.equals(City.class))
            return new City(code, name_RU, name_EN, latitude, longitude, findParent(parentCode, parentsList));
        if (type.equals(Airport.class))
            return new Airport(code, name_RU, name_EN, latitude, longitude, findParent(parentCode, parentsList));

        return null;

    }

}
