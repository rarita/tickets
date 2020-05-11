package ru.griga.tickets.ms_pathfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.repository.ItineraryRepository;
import ru.griga.tickets.shared.repository.SkyPickerItineraryRepository;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PathMakerService {

    private static final String MS_GATHERER_URL = "http://ms-gatherer";

    private final PathDiscoveryService pathDiscoveryService;
    private final ItineraryRepository itineraryRepository;
    private final RestTemplate restTemplate;
    private final SkyPickerItineraryRepository spir;

    public PathMakerService(PathDiscoveryService pathDiscoveryService,
                            ItineraryRepository itineraryRepository,
                            @Autowired @LoadBalanced RestTemplate restTemplate,
                            SkyPickerItineraryRepository spir) {

        this.pathDiscoveryService = pathDiscoveryService;
        this.itineraryRepository = itineraryRepository;
        this.restTemplate = restTemplate;
        this.spir = spir;

    }

    private List<String> makeChainPath (List<List<String>> initialData, String srcCode) {
        LinkedList<String> result = new LinkedList<>();

        String head = srcCode;
        // Каждый кусок оригинального листа всегда имеет размер 2
        // Dark sorcery #1
        for (List<String> bit : initialData) {
                result.add(head);

                // Dark sorcery #2
                head = bit.stream()
                        .filter((it) ->
                                !it.equals(result.getLast()))
                        .findFirst()
                        .orElseThrow();
        }

        result.add(head);
        return result;
    }

    /**
     * Отправить в микросервис сбора данных запросы
     * на поиск данных из входящей цепочки
     * @param chain Входящая цепочка перелетов
     * @param initSearchParams Параметры поиска, заданные пользователем
     * @return true - если запрос в микросервис сбора данных завершился без ошибки (код HTTP)
     */
    private boolean queryItinerariesFor(List<String> chain, SearchParams initSearchParams) {
        // Зачистим сдохшие по ttl перелеты т.к. будем опрашивать базу
        itineraryRepository.removeObsoleteItineraries();

        List<SearchParams> dataToQuery = new ArrayList<>();

        // Дату тут пока что манипулировать не буду
        for (int i = 0; i < chain.size() - 1; i++) {
            SearchParams searchParams = initSearchParams.clone();

            searchParams.setOriginCode(chain.get(i));
            searchParams.setDestinationCode(chain.get(i + 1));

            if (itineraryRepository.findItinerariesBySearchParams(searchParams).isEmpty())
                dataToQuery.add(searchParams);
        }

        // Если данных на запрос нет - запрос не посылаем
        if (dataToQuery.isEmpty())
            return true;

        // Закидываем на обработку в соседний микросервис
        var request = RequestEntity.method(HttpMethod.POST, URI.create(MS_GATHERER_URL + "/load/itineraries"))
                .accept(MediaType.APPLICATION_JSON)
                .body(dataToQuery);

        var typeRef = new ParameterizedTypeReference<Map<String, ?>>() {};
        var response = restTemplate.exchange(request, typeRef);

        // Проверочка на сохраненные - обновленные энтитис
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    /**
     * Находит возможные пути по заданным параметрам,
     * опираясь на возможные пути [:HAS_WAY], сохраненные в БД
     * @param searchParams Пользовательские параметры поиска
     * @return Статус запроса
     */
    public List<Map<?, ?>> makePathsFor(SearchParams searchParams) throws IOException {
        // Сначала проищем все пути из точки отправления
        pathDiscoveryService.discoverPathsFrom(searchParams.getOriginCode(), 2);

        // Добавим к возможным путям временный прямой путь, если он ещё не добавился
        Long tempWayID = null;
        if (!itineraryRepository.hasWay(searchParams.getOriginCode(), searchParams.getDestinationCode())) {
            tempWayID = itineraryRepository.makeWay(
                    searchParams.getOriginCode(),
                    searchParams.getDestinationCode(),
                    "1");
        }

        // Получаем список возможных путей хитрым запросом
        List<List<List<String>>> vWays
                = itineraryRepository.findViableWays(searchParams.getOriginCode(),
                                                     searchParams.getDestinationCode());

        List<List<String>> chains = vWays.stream()
                .map((it) -> makeChainPath(it, searchParams.getOriginCode()))
                .collect(Collectors.toList());

        // Отправить запросы на получение данных по направлениям в микросервис сбора данных
        for (List<String> chain : chains)
            queryItinerariesFor(chain, searchParams); // что-то сделать с результатом этого действа

        // На этом этапе в базе есть релевантные перелеты [:CAN_GO]
        // Ищем нужные маршруты ещё более хитрым запросом
        var itineraries = itineraryRepository.findItinerariesBySearchParams(searchParams);

        // Удалим временный прямой путь с левой ценой
        if (tempWayID != null)
            itineraryRepository.deleteWay(tempWayID);

        return itineraries;

    }
}
