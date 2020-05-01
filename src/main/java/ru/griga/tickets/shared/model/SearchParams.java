package ru.griga.tickets.shared.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchParams implements Cloneable {

    private String countryCode;
    private String currencyCode;
    private String locale;
    private String originCode;
    private String destinationCode;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate outboundDateFrom;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate outboundDateTo;

    private int adultsCount;
    private int childrenCount;
    private int infantsCount;

    private List<ItineraryType> typesAllowed
            = List.of(ItineraryType.AIRCRAFT, ItineraryType.BUS, ItineraryType.TRAIN);

    @Override
    public SearchParams clone() {
        return new SearchParams(
                countryCode,
                currencyCode,
                locale,
                originCode,
                destinationCode,
                outboundDateFrom,
                outboundDateTo,
                adultsCount,
                childrenCount,
                infantsCount,
                List.copyOf(typesAllowed)
        );
    }

}
