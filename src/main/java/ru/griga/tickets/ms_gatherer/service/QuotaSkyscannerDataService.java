package ru.griga.tickets.ms_gatherer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Value;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.model.SearchParams;

public class QuotaSkyscannerDataService implements BaseDataService {

    @Value("${skyscanner.get_quotas_url}")
    private final String GET_QUOTAS_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/" +
            "{country}/{currency}/{locale}/{originPlace}/{destinationPlace}/{outboundPartialDate}";

    private final UnirestInstance unirestInstance;
    private final ObjectMapper objectMapper;

    public QuotaSkyscannerDataService(UnirestInstance unirestInstance, ObjectMapper objectMapper) {
        this.unirestInstance = unirestInstance;
        this.objectMapper = objectMapper;
    }

    private GetRequest buildRequest(SearchParams searchParams) {

        return unirestInstance.get(GET_QUOTAS_URL)
                .routeParam("country", searchParams.getCountryCode())
                .routeParam("currency", searchParams.getCurrencyCode())
                .routeParam("locale", searchParams.getLocale())
                .routeParam("originPlace", searchParams.getOriginCode())
                .routeParam("destinationPlace", searchParams.getDestinationCode())
                .routeParam("outboundPartialDate", searchParams.getOutboundDateFrom().toString());
    }

    @Override
    public Object fetchData(SearchParams searchParams) throws UnirestException, JsonProcessingException {
        GetRequest quotaRequest = buildRequest(searchParams);
        HttpResponse<String> response = quotaRequest.asString();

        String responseString = response.getBody();

        return objectMapper.readValue(responseString, Itinerary[].class);
    }

}
