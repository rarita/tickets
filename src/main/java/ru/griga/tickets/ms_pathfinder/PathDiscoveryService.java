package ru.griga.tickets.ms_pathfinder;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.source.tree.Tree;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.repository.ItineraryRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PathDiscoveryService {

    private static final String ORIGIN_URL = "http://api.travelpayouts.com/v1/city-directions";

    private final ItineraryRepository itineraryRepository;
    private final UnirestInstance unirestInstance;
    private final ObjectMapper objectMapper;

    public PathDiscoveryService(ItineraryRepository itineraryRepository,
                                @Qualifier("travel-payouts") UnirestInstance unirestInstance,
                                ObjectMapper objectMapper) {

        this.itineraryRepository = itineraryRepository;
        this.unirestInstance = unirestInstance;
        this.objectMapper = objectMapper;

    }

    private GetRequest buildRQ(String originCode) {
        return unirestInstance.get(ORIGIN_URL)
                .queryString("origin", originCode)
                .queryString("currency", "rub");
    }

    /**
     * Получить от Travel Payouts список популярных направлений
     * из указанной точки
     * @param placeCode - IATA-код места отправления (страна или город)
     * @return список IATA-кодов популярных городов из этого места.
     * Это позволяет гарантировать, что между двумя точками резонно искать перемещения
     */
    Set<String> getPopularDirectionsFromPlace(String placeCode) throws IOException {
        HttpResponse<String> response = buildRQ(placeCode).asString();

        if (!response.isSuccess())
            throw new IOException("Request to Travel Payouts popular directions was unsuccessful: " +
                    response.getStatus() +
                    "\nFailed with a message: " + response.getBody());

        TreeNode tree = objectMapper.readTree(response.getBody());
        ObjectNode data = (ObjectNode) tree.get("data");

        Set<String> placeCodes = new TreeSet<>();
        data.fieldNames().forEachRemaining(placeCodes::add);
        return placeCodes;

    }

}
