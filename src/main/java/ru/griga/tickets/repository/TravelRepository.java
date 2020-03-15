package ru.griga.tickets.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ru.griga.tickets.model.Place;

public interface TravelRepository extends Neo4jRepository<Place, Long> {
}
