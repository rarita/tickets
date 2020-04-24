package ru.griga.tickets.shared.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import ru.griga.tickets.shared.model.itinerary.Itinerary;

import java.util.Set;

@Repository
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

    @Query("match (e:City)-[c:HAS_WAY]-(x:City) where e.code=$0 return count(c);")
    int countPossibleWaysFrom(String sourcePlaceCode);

    @Query("match (src:City),(dst:City) where src.code=$0 and dst.code=$1 " +
            "with point({latitude:src.latitude, longitude:src.longitude}) as s_loc, " +
            "point({latitude:dst.latitude, longitude:dst.longitude}) as d_loc, " +
            "src, dst " +
            "merge (src)-[:HAS_WAY{distance: distance(s_loc, d_loc)}]-(dst);")
    void makeWay(String sourcePlaceCode, String destPlaceCode);

}
