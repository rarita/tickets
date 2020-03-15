package ru.griga.tickets.service;

import kong.unirest.GetRequest;
import kong.unirest.UnirestException;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Value;
import ru.griga.tickets.model.SearchParams;

public class QuotaSkyscannerDataService implements BaseSkyscannerDataService {

    @Value("${skyscanner.get_quotas_url}")
    private final String GET_QUOTAS_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/" +
            "{country}/{currency}/{locale}/{originPlace}/{destinationPlace}/{outboundPartialDate}";

    private final UnirestInstance unirestInstance;

    public QuotaSkyscannerDataService(UnirestInstance unirestInstance) {
        this.unirestInstance = unirestInstance;
    }

    private GetRequest buildRequest(SearchParams searchParams) {

        return unirestInstance.get(GET_QUOTAS_URL)
                .routeParam("country", searchParams.getCountryCode())
                .routeParam("currency", searchParams.getCurrencyCode())
                .routeParam("locale", searchParams.getLocale())
                .routeParam("originPlace", searchParams.getOriginCode())
                .routeParam("destinationPlace", searchParams.getDestinationCode())
                .routeParam("outboundPartialDate", searchParams.getOutboundDate().toString());
    }

    @Override
    public Object fetchData(SearchParams searchParams) throws UnirestException {
        GetRequest quotaRequest = buildRequest(searchParams);
        return quotaRequest.asString();
    }

}
