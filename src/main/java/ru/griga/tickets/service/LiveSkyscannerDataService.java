package ru.griga.tickets.service;

import kong.unirest.*;
import org.springframework.beans.factory.annotation.Value;
import ru.griga.tickets.model.SearchParams;

public class LiveSkyscannerDataService implements BaseSkyscannerDataService {

    @Value("${skyscanner.create_session_url}")
    private final String CREATE_SESSION_URL = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0";
    private final UnirestInstance unirestInstance;

    public LiveSkyscannerDataService(UnirestInstance unirestInstance) {
        this.unirestInstance = unirestInstance;
    }

    private MultipartBody buildRequest(SearchParams sp) {

        return unirestInstance.post(CREATE_SESSION_URL)
                .field("country", sp.getCountryCode())
                .field("currency", sp.getCurrencyCode())
                .field("locale", sp.getLocale())
                .field("originPlace", sp.getOriginCode())
                .field("destinationPlace", sp.getDestinationCode())
                .field("outboundDate", sp.getOutboundDate().toString())
                .field("adults", Integer.toString(sp.getAdultsCount()));
    }

    @Override
    public Object fetchData(SearchParams searchParams) throws UnirestException {
        String sessionKey = createSession(searchParams);
        return pollSessionResults(sessionKey);
    }

    private String createSession(SearchParams searchParams) throws UnirestException {

        HttpResponse response = buildRequest(searchParams).asString();

        if (response.getStatus() != 201)
            throw new IllegalStateException("Server responded with non-OK code: " +
                    response.getStatus() + " " +
                    response.getStatusText() + " : " +
                    response.getBody());

        String location = response.getHeaders().getFirst("Location");
        int keyStartPosition = location.lastIndexOf('/') + 1;

        return location.substring(keyStartPosition);
    }

    private Object pollSessionResults(String sessionKey) {
        return null;
    }

}
