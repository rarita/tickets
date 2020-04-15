package ru.griga.tickets.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import ru.griga.tickets.model.itinerary.Itinerary;
import ru.griga.tickets.model.place.base.Place;

import java.util.Set;

public interface TravelRepository extends Neo4jRepository<Place, String> {

}
