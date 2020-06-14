package ru.griga.tickets.shared.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.model.itinerary.Itinerary;

import java.util.*;

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

    @Query("MATCH (src:City)<-[:IS_IN]-(a_src:Airport)-[itin:CAN_GO]->(a_dest:Airport)-[:IS_IN]->(dest:City) " +
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
            "merge (src)-[way:HAS_WAY{distance: distance(s_loc, d_loc), price: $2}]-(dst)" +
            "return id(way);")
    long makeWay(String sourcePlaceCode, String destPlaceCode, String price);

    @Query("match (p:City {code: $0}),(b:City {code: $1}) " +
            "RETURN EXISTS( (p)-[:HAS_WAY]-(b) )")
    Boolean hasWay(String sourcePlaceCode, String destPlaceCode);

    @Query("match ()-[e:HAS_WAY]-() where e.id=$0 delete e;")
    void deleteWay(long wayID);

    // Максимум прыжков всегда будет 2
    // UNWIND на cl_batch не работает
    @Query("match q=((src:City {code: $0})-[way:HAS_WAY*..2]-(dst:City {code: $1})) " +
            "with [x in relationships(q) | [ startNode(x).code, endNode(x).code]] as batch, " +
            "apoc.coll.sum([x in relationships(q) | x.distance]) as dist, " +
            "apoc.coll.sum([x in relationships(q) | toFloat(x.price)]) as price " +
            "with DISTINCT dist, head(collect(batch)) as cl_batch, head(collect(price)) as price " +
            "return cl_batch ORDER BY dist,price LIMIT 3;")
    List<List<List<String>>> findViableWays(String sourcePlaceCode, String destPlaceCode);

    // Передаем searchParams и получаем доступ наподобие $searchParams.prop
    // НЕ используем local-datetime так как у нас все должно быть в UTC-0
    @Query("match (air_src:Airport), (air_dst:Airport),(c_src:City {code: $searchParams.originCode}),(c_dst:City {code: $searchParams.destinationCode}),\n" +
            "path = (c_src)<-[src_in:IS_IN]-(air_src)-[itin:CAN_GO*..2]-(air_dst)-[dst_in:IS_IN]->(c_dst)\n" +
            "where none(idx in range(0, size(itin) - 2) where datetime(itin[idx].arrivalTime) >= datetime(itin[idx+1].departureTime))\n" +
            "and all(i in itin where i.type in $searchParams.typesAllowed and (date(datetime(i.departureTime)) >= date($searchParams.outboundDateFrom) " +
            "and date(datetime(i.departureTime)) <= date($searchParams.outboundDateTo)))\n" +
            "return c_src, c_dst, itin, air_src as src, air_dst as dst, path;")
    List<Map<?, ?>> findItinerariesBySearchParams(SearchParams searchParams);

    // Передаем searchParams и получаем доступ наподобие $searchParams.prop
    // НЕ используем local-datetime так как у нас все должно быть в UTC-0
    @Query("match (air_src:Airport), (air_dst:Airport),(c_src:City {code: $searchParams.originCode}),(c_dst:City {code: $searchParams.destinationCode}),\n" +
            "path = (c_src)<-[src_in:IS_IN]-(air_src)-[itin:CAN_GO*..2]-(air_dst)-[dst_in:IS_IN]->(c_dst)\n" +
            "where none(idx in range(0, size(itin) - 2) where datetime(itin[idx].arrivalTime) >= datetime(itin[idx+1].departureTime))\n" +
            "and all(i in itin where i.type in $searchParams.typesAllowed and (date(datetime(i.departureTime)) >= date($searchParams.outboundDateFrom) " +
            "and date(datetime(i.departureTime)) <= date($searchParams.outboundDateTo)))\n" +
            "return path;")
    List<Map<?, ?>> findItinerariesBySearchParams_tst(SearchParams searchParams);

    @Query("match ()-[e:CAN_GO]-() return date(datetime(e.departureTime)), $searchParams.outboundDateFrom, " +
            "date($searchParams.outboundDateFrom), date(datetime(e.departureTime)) >= date($searchParams.outboundDateFrom)  limit 1;")
    List<Map<?, ?>> testQuery(SearchParams searchParams);

    @Query("match (e:City)-[:IS_IN]->(x:Country) \n" +
            "\n" +
            "where toLower(e.name_EN) contains toLower($0) or\n" +
            "(exists(e.name_RU) and toLower(e.name_RU) contains toLower($0)) " +
            "or (e.code contains toUpper($0))\n" +
            "return e.code as id,\n" +
            "case\n" +
            "when (exists(e.name_RU))\n" +
            "then e.name_RU\n" +
            "else e.name_EN\n" +
            "end + ', ' +\n" +
            "case\n" +
            "when (exists(x.name_RU))\n" +
            "then x.name_RU\n" +
            "else x.name_EN\n" +
            "end as value\n" +
            "order by size(value) limit 5")
    List<Map<String, String>> getAutocompletionData(String query);

}
