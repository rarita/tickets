package ru.griga.tickets.ms_pathfinder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.griga.tickets.ms_pathfinder.service.PathMakerService;
import ru.griga.tickets.shared.model.SearchParams;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {

    private final PathMakerService pathMakerService;

    public MainController(PathMakerService pathMakerService) {
        this.pathMakerService = pathMakerService;
    }

    @GetMapping("/fuck")
    public ResponseEntity<String> testMapping() {
        return ResponseEntity.ok("fok u");
    }

    @PostMapping("/paths_for")
    public ResponseEntity<?> getPathsFor(@RequestBody SearchParams searchParams) throws IOException {
        var response = pathMakerService.makePathsFor(searchParams);
        return ResponseEntity.ok(response);
    }

}
