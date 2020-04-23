package ru.griga.tickets.shared.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import ru.griga.tickets.shared.model.place.base.Place;

@Repository
public interface TravelRepository extends Neo4jRepository<Place, String> {

}
