package ru.griga.tickets.ms_pathfinder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.griga.tickets.ms_pathfinder.service.PathMakerService;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.model.itinerary.Itinerary;
import ru.griga.tickets.shared.repository.ItineraryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class MainController {

    private final PathMakerService pathMakerService;
    private final ItineraryRepository itineraryRepository;

    public MainController(PathMakerService pathMakerService, ItineraryRepository itineraryRepository) {
        this.pathMakerService = pathMakerService;
        this.itineraryRepository = itineraryRepository;
    }

    @PostMapping("/paths_for")
    public ResponseEntity<?> getPathsFor(@RequestBody SearchParams searchParams) throws IOException {
        var response = pathMakerService.makePathsFor(searchParams);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/autocomplete")
    public List<Map<String, String>> getAutocompletionValue(@RequestParam("term") String query) {
        return itineraryRepository.getAutocompletionData(query);
    }

}
