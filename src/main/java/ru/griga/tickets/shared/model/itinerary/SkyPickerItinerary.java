package ru.griga.tickets.shared.model.itinerary;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import ru.griga.tickets.shared.model.ItineraryType;
import ru.griga.tickets.shared.model.place.base.Place;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RelationshipEntity("CAN_GO")
@EqualsAndHashCode(callSuper = true)
public class SkyPickerItinerary extends Itinerary {

    private ItineraryType type;

    // Class-specific поля
    private String fareBasis;
    private String fareCategory;
    private String fareClasses;
    private String fareFamily;

    private int flightNumber;
    @EqualsAndHashCode.Exclude
    private String aircraftInfo; // -> equipment, иногда не приходит из скайпикера (Non) поэтому выкидываем из EAHC

    private String airlineCode;

    @EqualsAndHashCode.Exclude // вполне может меняться, лучше исключить
    private String bookingLink;

    public SkyPickerItinerary() {

    }

    @Builder
    public SkyPickerItinerary(Long id, Place source, Place destination, BigDecimal cost, BigDecimal baseCost, String currency, String carrierCode, LocalDateTime departureTime, LocalDateTime arrivalTime, LocalDateTime foundAt, long ttl, ItineraryType type, String fareBasis, String fareCategory, String fareClasses, String fareFamily, int flightNumber, String aircraftInfo, String airlineCode, String bookingLink) {
        super(id, source, destination, cost, baseCost, currency, carrierCode, departureTime, arrivalTime, foundAt, ttl);
        this.type = type;
        this.fareBasis = fareBasis;
        this.fareCategory = fareCategory;
        this.fareClasses = fareClasses;
        this.fareFamily = fareFamily;
        this.flightNumber = flightNumber;
        this.aircraftInfo = aircraftInfo;
        this.airlineCode = airlineCode;
        this.bookingLink = bookingLink;
    }

}
