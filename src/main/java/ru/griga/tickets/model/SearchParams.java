package ru.griga.tickets.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchParams {

    private final String countryCode;
    private final String currencyCode;
    private final String locale;
    private final String originCode;
    private final String destinationCode;
    private final LocalDate outboundDate;
    private final int adultsCount;

}
