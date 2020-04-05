package ru.griga.tickets.model.place.base;

import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import ru.griga.tickets.model.Itinerary;

import java.util.Set;

@Data
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public abstract class Place {

    @Id
    @Setter(AccessLevel.NONE)
    private String code;

    @Setter(AccessLevel.NONE)
    private String name_RU;
    @Setter(AccessLevel.NONE)
    private String name_EN;

    @Relationship(type = "IS_IN")
    private Place parent;

}