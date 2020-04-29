package ru.griga.tickets.ms_pathfinder.service;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Service
public class TravelPayoutsPopularDirectionsService {

    private static final String ORIGIN_URL = "http://api.travelpayouts.com/v1/city-directions";

    private final UnirestInstance unirestInstance;
    private final ObjectMapper objectMapper;

    public TravelPayoutsPopularDirectionsService(@Qualifier("travel-payouts") UnirestInstance unirestInstance,
                                                 ObjectMapper objectMapper) {

        this.unirestInstance = unirestInstance;
        this.objectMapper = objectMapper;

    }

    private GetRequest buildRQ(String originCode) {
        return unirestInstance.get(ORIGIN_URL)
                .queryString("origin", originCode)
                .queryString("currency", "eur");
    }

    /**
     * Получить от Travel Payouts список популярных направлений
     * из указанной точки
     * @param placeCode - IATA-код места отправления (страна или город)
     * @return список IATA-кодов популярных городов из этого места.
     * Это позволяет гарантировать, что между двумя точками резонно искать перемещения
     */
    public Map<String, BigDecimal> getPopularDirectionsFromPlace(String placeCode) throws IOException {
        HttpResponse<String> response = buildRQ(placeCode).asString();

        if (!response.isSuccess())
            throw new IOException("Request to Travel Payouts popular directions was unsuccessful: " +
                    response.getStatus() +
                    "\nFailed with a message: " + response.getBody());

        TreeNode tree = objectMapper.readTree(response.getBody());
        ObjectNode data = (ObjectNode) tree.get("data");

        Map<String, BigDecimal> placeData = new TreeMap<>();

        data.fieldNames()
                .forEachRemaining((code) -> placeData.put(
                        code,
                        new BigDecimal(Utils.getNodeValue(data.get(code), "price", null))));

        return placeData;

    }

}
