package ru.griga.tickets.shared.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ru.griga.tickets.shared.model.place.base.Place;

public interface TravelRepository extends Neo4jRepository<Place, String> {

}
