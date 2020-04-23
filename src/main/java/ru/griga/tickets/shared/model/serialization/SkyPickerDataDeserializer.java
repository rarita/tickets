package ru.griga.tickets.shared.model.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.griga.tickets.shared.model.ItineraryType;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.itinerary.SkyPickerItinerary;
import ru.griga.tickets.shared.model.place.base.Place;
import ru.griga.tickets.shared.repository.CarrierRepository;
import ru.griga.tickets.shared.repository.TravelRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static ru.griga.tickets.shared.Utils.getNodeValue;

public class SkyPickerDataDeserializer extends StdDeserializer<Itinerary> {

    private final TravelRepository travelRepository;
    private final CarrierRepository carrierRepository;

    public SkyPickerDataDeserializer(TravelRepository travelRepository,
                                     CarrierRepository carrierRepository) {
        super(Itinerary.class);

        this.travelRepository = travelRepository;
        this.carrierRepository = carrierRepository;

    }

    private ItineraryType getItinTypeFromString(String type) {
        switch (type) {
            case "aircraft":
                return ItineraryType.AIRCRAFT;
            case "bus":
                return ItineraryType.BUS;
            case "train":
                return ItineraryType.TRAIN;
            default:
                throw new IllegalArgumentException("No such itinerary type: " + type);
        }
    }

    @Override
    public Itinerary deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var tree = p.getCodec().readTree(p);

        var route = tree.get("route").get(0);

        // Получаем параметры маршрута
        String sourcePlaceCode = getNodeValue(route, "flyFrom", null);
        Place source = travelRepository.findById(sourcePlaceCode)
                .orElseThrow();

        String destPlaceCode = getNodeValue(route, "flyTo", null);
        Place destination = travelRepository.findById(destPlaceCode)
                .orElseThrow();

        BigDecimal cost = new BigDecimal(getNodeValue(tree, "price", null));

        TreeNode conversion = tree.get("conversion");

        // Первое поле конверсии обозначает нужную валюту
        String currencyCode = conversion.fieldNames().next();
        // Отсюда же можно получить цену билета в евро, что подходит для базовой цены
        BigDecimal baseCost = new BigDecimal(getNodeValue(conversion, "EUR", null));

        String airlineCode = getNodeValue(route, "airline", "");
        String carrierCode = getNodeValue(route, "operating_carrier", "");

        if (airlineCode.isBlank() && carrierCode.isBlank())
            throw new IllegalArgumentException("Both Airline code and Carrier code are blank.");

        if (airlineCode.isBlank())
            airlineCode = carrierCode;
        else if (carrierCode.isBlank())
            carrierCode = airlineCode;

        if (!carrierRepository.existsById(carrierCode))
            throw new IllegalArgumentException("Carrier code does not exist (carrier): " + carrierCode);
        if (!carrierRepository.existsById(airlineCode))
            throw new IllegalArgumentException("Carrier code does not exist (airline): " + airlineCode);

        LocalDateTime departureTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(Long.parseLong(getNodeValue(route, "dTimeUTC", null))),
                TimeZone.getTimeZone("UTC+0").toZoneId()
        );

        LocalDateTime arrivalTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(Long.parseLong(getNodeValue(route, "aTimeUTC", null))),
                TimeZone.getTimeZone("UTC+0").toZoneId()
        );

        LocalDateTime foundAt = LocalDateTime.now();

        long ttl = Duration.ofHours(6).toSeconds(); // todo инжектить возможно. Хотя раз мы знаем что парсим скайпикер то можно и не инжектить

        // Поля для подкласса SkyPickerItinerary
        String typeString = getNodeValue(route, "vehicle_type", "aircraft");
        ItineraryType type = getItinTypeFromString(typeString);

        String fareBasis = getNodeValue(route, "fare_basis", null);
        String fareCategory = getNodeValue(route, "fare_category", null);
        String fareClasses = getNodeValue(route, "fare_classes", null);
        String fareFamily = getNodeValue(route, "fare_family", null);

        int flightNumber = Integer.parseInt(getNodeValue(route, "", "0"));

        String aircraftInfo = getNodeValue(route, "equipment", null);
        String bookingLink = getNodeValue(tree, "deep_link", "https://kiwi.com");

        return SkyPickerItinerary.builder()
                .id(null)
                .source(source)
                .destination(destination)
                .cost(cost)
                .baseCost(baseCost)
                .currency(currencyCode)
                .carrierCode(carrierCode)
                .airlineCode(airlineCode)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .foundAt(foundAt)
                .ttl(ttl)
                .type(type)
                .fareBasis(fareBasis)
                .fareCategory(fareCategory)
                .fareClasses(fareClasses)
                .fareFamily(fareFamily)
                .flightNumber(flightNumber)
                .aircraftInfo(aircraftInfo)
                .bookingLink(bookingLink)
                .build();

    }

}
