package ru.griga.tickets.ms_gatherer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.model.place.Airport;
import ru.griga.tickets.shared.model.place.City;
import ru.griga.tickets.shared.model.place.Country;
import ru.griga.tickets.shared.model.place.base.Place;
import ru.griga.tickets.shared.repository.TravelRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TravelPayoutsGeoService {

    private static final String origin = "https://api.travelpayouts.com/data/ru/";

    private final UnirestInstance unirestInstance;
    private final ObjectMapper objectMapper;
    private final TravelRepository repository;


    public TravelPayoutsGeoService(@Qualifier("clean") UnirestInstance unirestInstance,
                                   ObjectMapper objectMapper,
                                   TravelRepository repository) {

        this.unirestInstance = unirestInstance;
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    private String getJsonListOf(String kind) throws IOException {
        var response = unirestInstance.get(origin + kind + ".json")
                .asString();

        if (!response.isSuccess())
            throw new IOException("Countries request failed with a code " + response.getStatus());

        return response.getBody();
    }

    private <T extends Place> Place[] deserializeListOf(Class<?> clazz,
                                                        String json,
                                                        String parentNodeName,
                                                        List<T> parents) throws JsonProcessingException {

        var injectableValues = new InjectableValues.Std();
        injectableValues.addValue("class", clazz);
        injectableValues.addValue("parentsList", parents);
        injectableValues.addValue("parentNodeName", parentNodeName);

        objectMapper.setInjectableValues(injectableValues);
        return objectMapper.readValue(json, Place[].class);

    }

    public List<Country> getCountries() throws IOException {
        var responseString = getJsonListOf("countries");

        var countries = deserializeListOf(Country.class, responseString, null, null);

        return Stream.of(countries)
                .map((item) -> (Country) item)
                .collect(Collectors.toList());
    }

    public List<City> getCities(List<Country> countries) throws IOException {
        var responseString = getJsonListOf("cities");

        var cities = deserializeListOf(City.class, responseString, "country_code", countries);

        return Stream.of(cities)
                .map((item) -> (City) item)
                .collect(Collectors.toList());
    }

    public List<Airport> getAirports(List<City> cities) throws IOException {
        var responseString = getJsonListOf("airports");

        var airports = deserializeListOf(Airport.class, responseString, "city_code", cities);

        return Stream.of(airports)
                .map((item) -> (Airport) item)
                .collect(Collectors.toList());
    }

    public List<? extends Place> getLocations() throws IOException {
        var countries = getCountries();
        var cities = getCities(countries);
        return getAirports(cities);
    }

    public Map<String, String> populateDatabase() throws IOException {
        var countries = getCountries();
        var cities = getCities(countries);
        var airports = getAirports(cities);

        var start = System.currentTimeMillis();
        repository.saveAll(countries);
        repository.saveAll(cities);
        repository.saveAll(airports);
        var end = System.currentTimeMillis();

        return Map.of(
                "status", "OK",
                "entities-loaded", String.valueOf(airports.size()),
                "time-spent-saving", (end - start) + " ms.");
    }

}
