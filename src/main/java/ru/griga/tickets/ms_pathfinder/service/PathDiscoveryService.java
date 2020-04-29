package ru.griga.tickets.ms_pathfinder.service;

import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.repository.ItineraryRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Определяет возможные соединения нод графа
 * по которым впоследствии будут строиться маршруты с пересадками
 */
@Service
public class PathDiscoveryService {

    private final ItineraryRepository itineraryRepository;
    private final TravelPayoutsPopularDirectionsService tppds;

    public PathDiscoveryService(ItineraryRepository itineraryRepository, TravelPayoutsPopularDirectionsService tppds) {
        this.itineraryRepository = itineraryRepository;
        this.tppds = tppds;
    }

    /**
     * Раскрывает возможные пути перемещения из точки отправления
     * на заданном числе прыжков. Данные берутся из популярных направлений по городам
     * @param originCode IATA-код города отправления
     * @param hops Количество прыжков из отправной точки
     * @return Статус-объект
     * @throws IOException если не вернулся ответ из сети
     */
    public Map<String, String> discoverPathsFrom(String originCode, int hops) throws IOException {

        Set<String> targets = new TreeSet<>();
        targets.add(originCode);

        int rqCount = 0;
        int pathsWritten = 0;

        for (int i = 0; i < hops; i++) {
            Set<String> nextTargets = new TreeSet<>();

            for (String targetCode : targets) {
                // Если для этого города уже найдены возможные пути
                if (itineraryRepository.countPossibleWaysFrom(targetCode) >= 10)
                    continue;

                Map<String, BigDecimal> waysFromTarget = tppds.getPopularDirectionsFromPlace(targetCode);
                rqCount ++;

                for (Map.Entry<String, BigDecimal> destEntry : waysFromTarget.entrySet()) {
                    itineraryRepository.makeWay(targetCode, destEntry.getKey(), destEntry.getValue().toString());
                    pathsWritten ++;
                }

                nextTargets.addAll(waysFromTarget.keySet());
            }
            targets = nextTargets;
        }

        return Map.of("status", "OK",
                    "web-requests-sent", String.valueOf(rqCount),
                    "paths-saved-db", String.valueOf(pathsWritten));
    }

}
