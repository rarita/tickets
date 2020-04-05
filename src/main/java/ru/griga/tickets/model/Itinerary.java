package ru.griga.tickets.model;

import lombok.Data;
import org.neo4j.ogm.annotation.*;
import ru.griga.tickets.model.place.base.Place;

import java.math.BigDecimal;

@Data
@RelationshipEntity(type = "CAN_GO")
public class Itinerary {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private final Place source;

    @EndNode
    private final Place destination;

    private final BigDecimal cost;

}