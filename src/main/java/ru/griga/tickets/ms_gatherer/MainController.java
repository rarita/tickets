package ru.griga.tickets.ms_gatherer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.model.place.Airport;
import ru.griga.tickets.shared.model.place.base.Place;
import ru.griga.tickets.shared.repository.TravelRepository;
import ru.griga.tickets.ms_gatherer.service.SkyPickerDataService;
import ru.griga.tickets.ms_gatherer.service.TravelPayoutsCarrierService;
import ru.griga.tickets.ms_gatherer.service.TravelPayoutsGeoService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class MainController {

    private final TravelRepository repository;
    private final TravelPayoutsGeoService tpgs;
    private final TravelPayoutsCarrierService tpcs;
    private final SkyPickerDataService spds;

    public MainController(TravelRepository repository,
                          TravelPayoutsGeoService tpgs,
                          TravelPayoutsCarrierService tpcs,
                          SkyPickerDataService spds) {

        this.repository = repository;
        this.tpgs = tpgs;
        this.tpcs = tpcs;
        this.spds = spds;

    }

    @GetMapping("/save")
    @ResponseBody
    public ResponseEntity<String> save() {
        return ResponseEntity.ok("Service unused");
    }

    @GetMapping("/populate/geo")
    @ResponseBody
    public ResponseEntity<Map<String, String>> populateGeo() {

        try {
            return ResponseEntity.ok(tpgs.populateDatabase());
        }
        catch (IOException exc) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", exc.getMessage(),
                    "trace", Arrays.toString(exc.getStackTrace())
            ));
        }

    }

    /**
     * Запрос на заливку в базу авиаперевозчиков / авиакомпаний
     * @return статус заливки
     */
    @GetMapping("/populate/carriers")
    @ResponseBody
    public ResponseEntity<Map<String, String>> populateCarriers() {

        try {
            return ResponseEntity.ok(tpcs.populateCarriers());
        }
        catch (IOException exc) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", exc.getMessage(),
                    "trace", Arrays.toString(exc.getStackTrace())
            ));
        }

    }

    @PostMapping("/load/itineraries")
    @ResponseBody
    public ResponseEntity<?> loadItineraries
            (@RequestBody List<SearchParams> searchParamsList) {

        var messages = searchParamsList.stream()
                .map((searchParams -> {
                    try {
                        return spds.persistData(searchParams);
                    }
                    catch (IOException exc) {
                        return Map.of(
                                "status", "error",
                                "message", exc.getMessage(),
                                "trace", Arrays.toString(exc.getStackTrace()));
                    }
                }))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("status", "OK",
                                        "messages", messages));

    }

}
