package ru.griga.tickets.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import ru.griga.tickets.model.itinerary.Itinerary;

import java.util.List;
import java.util.Set;

public interface ItineraryRepository extends Neo4jRepository<Itinerary, Long> {

    /**
     * Нужно обязательно прописывать в return все ноды / отношения (или *)
     * чтобы OGM смог это превратить в POJO
     * @return List или Set перелетов в системе.
     */
    @Query("MATCH (src:Place)-[itin:CAN_GO]->(dest:Place) " +
            "return *;")
    Set<Itinerary> findEveryItinerary();

    @Query("MATCH (src:Place)-[itin:CAN_GO]->(dest:Place) " +
            "where src.code=$0 AND dest.code=$1 " +
            "return *;")
    Set<Itinerary> findItinerariesBetween(String sourcePlaceCode, String destPlaceCode);

    @Query("match (x)-[e:CAN_GO]->(y) " +
            "where duration.between(localdatetime(e.foundAt), localdatetime()).seconds > e.ttl " +
            "delete e;")
    void removeObsoleteItineraries();

}
