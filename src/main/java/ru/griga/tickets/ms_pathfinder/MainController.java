package ru.griga.tickets.ms_pathfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.griga.tickets.ms_pathfinder.service.PathMakerService;
import ru.griga.tickets.shared.model.SearchParams;
import ru.griga.tickets.shared.repository.ItineraryRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class MainController {

    private final PathMakerService pathMakerService;
    private final ItineraryRepository itineraryRepository;
    private final ObjectMapper objectMapper;

    public MainController(PathMakerService pathMakerService,
                          ItineraryRepository itineraryRepository,
                          ObjectMapper objectMapper) {

        this.pathMakerService = pathMakerService;
        this.itineraryRepository = itineraryRepository;
        this.objectMapper = objectMapper;

    }

    @PostMapping("/paths_for")
    public ResponseEntity<?> getPathsFor(@RequestBody SearchParams searchParams) throws IOException, InterruptedException {
        //if (true) return getPathsForStub();

        var response = pathMakerService.makePathsFor(searchParams);
        return ResponseEntity.ok(objectMapper.writeValueAsString(response));
    }

    @PostMapping("/paths_for_stub")
    public ResponseEntity<?> getPathsForStub() throws IOException, InterruptedException {
        var responseString = Files.readString(Path.of("sex2.json"), Charset.defaultCharset());
        Thread.sleep(1000);
        return ResponseEntity.ok(responseString);
    }

    @GetMapping("/autocomplete")
    public List<Map<String, String>> getAutocompletionValue(@RequestParam("term") String query) {
        return itineraryRepository.getAutocompletionData(query);
    }

}
