package ru.griga.tickets.ms_pathfinder;

import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.repository.ItineraryRepository;
import ru.griga.tickets.shared.repository.TravelRepository;

import java.io.IOException;
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

                Set<String> waysFromTarget = tppds.getPopularDirectionsFromPlace(targetCode);
                rqCount ++;

                for (String destCode : waysFromTarget) {
                    itineraryRepository.makeWay(targetCode, destCode);
                    pathsWritten ++;
                }

                nextTargets.addAll(waysFromTarget);
            }
            targets = nextTargets;
        }

        return Map.of("status", "OK",
                    "web-requests-sent", String.valueOf(rqCount),
                    "paths-saved-db", String.valueOf(pathsWritten));
    }

}
