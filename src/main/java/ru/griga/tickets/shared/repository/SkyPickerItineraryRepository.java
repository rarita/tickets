package ru.griga.tickets.shared.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.itinerary.SkyPickerItinerary;

import java.util.List;
import java.util.Map;

public interface SkyPickerItineraryRepository extends Neo4jRepository<SkyPickerItinerary, Long> {

    // Передаем searchParams и получаем доступ наподобие $searchParams.prop
    // НЕ используем local-datetime так как у нас все должно быть в UTC-0
    @Query("match p=(c_src:City {code: $searchParams.originCode})<-[:IS_IN]-(src:Airport)-[itin:CAN_GO*..3]->(dst:Airport)-[:IS_IN]->(c_dst:City) " +
            "where last(nodes(p)).code = $searchParams.destinationCode and none(idx in range(0, size(itin) - 2) " +
            "where datetime(itin[idx].arrivalTime) >= datetime(itin[idx+1].departureTime)) " +
            "and all(i in itin " +
            "where i.type in $searchParams.typesAllowed and " + // and
            "(date(datetime(i.departureTime)) >= date($searchParams.outboundDateFrom) " +
            "and date(datetime(i.departureTime)) <= date($searchParams.outboundDateTo))" + // тут про дату
            ") " +
            "return c_src, src, itin, dst, c_dst;")
    List<Map<?,?>> findItinerariesBySearchParams(SearchParams searchParams);

}
