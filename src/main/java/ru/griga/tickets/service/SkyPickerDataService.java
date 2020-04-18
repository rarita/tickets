package ru.griga.tickets.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import kong.unirest.UnirestInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.griga.tickets.model.ItineraryType;
import ru.griga.tickets.model.SearchParams;
import ru.griga.tickets.model.itinerary.Itinerary;
import ru.griga.tickets.repository.ItineraryRepository;
import ru.griga.tickets.repository.TravelRepository;

import java.io.DataInput;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkyPickerDataService implements BaseDataService {

    private static final String ORIGIN_URL = "https://api.skypicker.com/flights";

    private static final DateTimeFormatter skyPickerDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ItineraryRepository itineraryRepository;

    private final UnirestInstance unirestInstance;
    private final ObjectMapper objectMapper;

    private String getVehicleTypes(List<ItineraryType> types) {
        return types.stream()
                .map(ItineraryType::toString)
                .collect(Collectors.joining(","));
    }

    private GetRequest buildRQ(SearchParams searchParams) {
        return unirestInstance.get(ORIGIN_URL)
                .queryString("fly_from", searchParams.getOriginCode())
                .queryString("fly_to", searchParams.getDestinationCode())
                .queryString("date_from", searchParams.getOutboundDateFrom().format(skyPickerDateFormatter))
                .queryString("date_to", searchParams.getOutboundDateTo().format(skyPickerDateFormatter))
                .queryString("curr", searchParams.getCurrencyCode())
                .queryString("locale", searchParams.getLocale().substring(0, 2))
                .queryString("partner", "picky")
                .queryString("max_stopovers", 0) // Пока что будем использовать только прямые рейсы пока не знаем че с ценой
                .queryString("adults", searchParams.getAdultsCount())
                .queryString("children", searchParams.getChildrenCount())
                .queryString("infants", searchParams.getInfantsCount())
                .queryString("vehicle_type", getVehicleTypes(searchParams.getTypesAllowed()))
                .queryString("v", 3);
    }

    public SkyPickerDataService(ItineraryRepository itineraryRepository,
                                @Qualifier("clean") UnirestInstance unirestInstance,
                                ObjectMapper objectMapper) {

        this.itineraryRepository = itineraryRepository;
        this.unirestInstance = unirestInstance;
        this.objectMapper = objectMapper;

    }


    @Override
    public List<Itinerary> fetchData(SearchParams searchParams) throws UnirestException, IOException {
        GetRequest rq = buildRQ(searchParams);

        // Для начала надо создать ссылку на JSON-массив "data" в ответе SkyPicker
        HttpResponse<String> response = rq.asString();

        if (!response.isSuccess())
            throw new IOException("Request to SkyPicker was unsuccessful: " + response.getStatus() +
                    "\nFailed with a message: " + response.getBody());

        String itineraryDataTree = objectMapper.readTree(response.getBody())
                .get("data")
                .toString();

        CollectionType typeReference = TypeFactory.defaultInstance()
                .constructCollectionType(List.class, Itinerary.class);

        return objectMapper.readValue(itineraryDataTree, typeReference);

    }

    public Map<String, String> persistData(SearchParams searchParams) throws IOException {
        var itineraries = fetchData(searchParams);

        var start = System.currentTimeMillis();
        // Логика сохранения через ItineraryRepository (с удалением пажылых нод)

        // - Получаем уже сохранённые перелеты из базы, перед этим зачистив базу от устаревших моментов
        itineraryRepository.removeObsoleteItineraries();
        var savedItineraries = itineraryRepository.findItinerariesBetween(searchParams.getOriginCode(), searchParams.getDestinationCode());

        // Удаляем все пересекающиеся с найденными, т.к. найденные будут свежеé
        savedItineraries.retainAll(itineraries); // мутирует savedItineraries так что больше этот сет не юзаем
        itineraryRepository.deleteAll(savedItineraries);

        // Наконец сохраняем то что накачали
        itineraryRepository.saveAll(itineraries);

        var end = System.currentTimeMillis();

        return Map.of(
                "status", "OK",
                "rq", searchParams.toString(),
                "entities-updated", String.valueOf(savedItineraries.size()),
                "entities-saved", String.valueOf(itineraries.size() - savedItineraries.size()),
                "time-spent-saving", (end - start) + " ms.");
    }

}
