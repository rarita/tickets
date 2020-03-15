package ru.griga.tickets.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@Data
@NodeEntity
public class Place {

    @Id
    @GeneratedValue
    private final Long id;

    private final String code;
    private final String name;
    private final String type;

    @Relationship(type = "IS_IN")
    private Place parent;

    @Relationship(type = "CAN_GO")
    @EqualsAndHashCode.Exclude
    private Set<Itinerary> itineraries;

}