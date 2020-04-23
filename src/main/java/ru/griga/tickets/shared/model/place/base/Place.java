package ru.griga.tickets.shared.model.place.base;

import lombok.*;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

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
    @EqualsAndHashCode.Exclude
    private Place parent;

}