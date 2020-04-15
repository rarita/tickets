package ru.griga.tickets.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SearchParams {

    private final String countryCode;
    private final String currencyCode;
    private final String locale;
    private final String originCode;
    private final String destinationCode;
    private final LocalDate outboundDateFrom;
    private final LocalDate outboundDateTo;

    private final int adultsCount;
    private final int childrenCount;
    private final int infantsCount;

    private final List<ItineraryType> typesAllowed;

}
