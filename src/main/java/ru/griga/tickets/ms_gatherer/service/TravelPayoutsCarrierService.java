package ru.griga.tickets.ms_gatherer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.model.Carrier;
import ru.griga.tickets.shared.repository.CarrierRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class TravelPayoutsCarrierService {

    private final UnirestInstance unirestInstance;
    private final ObjectMapper objectMapper;

    private final CarrierRepository carrierRepository;

    public TravelPayoutsCarrierService(@Qualifier("clean") UnirestInstance unirestInstance,
                                       ObjectMapper objectMapper,
                                       CarrierRepository carrierRepository) {

        this.unirestInstance = unirestInstance;
        this.objectMapper = objectMapper;
        this.carrierRepository = carrierRepository;

    }

    public List<Carrier> getCarriers() throws IOException {

        var response = unirestInstance.get("http://api.travelpayouts.com/data/ru/airlines.json").asString();

        if (!response.isSuccess())
            throw new IOException("Can't get airlines from TravelPayouts: " + response.getStatus() + " " + response.getStatusText());

        var carrierArray = objectMapper.readValue(response.getBody(), Carrier[].class);

        return Arrays.asList(carrierArray);

    }

    public Map<String, String> populateCarriers() throws IOException {
        var carriers = getCarriers();
        carrierRepository.saveAll(carriers);

        return Map.of("status", "OK");
    }

}
