package ru.griga.tickets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.net.URL;

/**
 * Перевозчик (авиа / жд), либо авиакомпания, выписавшая билет.
 */
@Data
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public class Carrier {

    @Id
    private String code;
    private String name;

    private URL logoURL;

}
