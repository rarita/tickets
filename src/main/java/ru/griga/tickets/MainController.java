package ru.griga.tickets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.griga.tickets.model.place.Airport;
import ru.griga.tickets.model.place.base.Place;
import ru.griga.tickets.repository.TravelRepository;
import ru.griga.tickets.service.TravelPayoutsGeoService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;


@Controller
@RequestMapping("/")
public class MainController {

    private final TravelRepository repository;
    private final TravelPayoutsGeoService tpgs;

    public MainController(TravelRepository repository, TravelPayoutsGeoService tpgs) {
        this.repository = repository;
        this.tpgs = tpgs;
    }

    @GetMapping("/save")
    @ResponseBody
    public Iterable<Place> save() {
        var air = new Airport("LED", "Пулково", "Pulkovo", 0.1, 1.0, null);
        repository.save(air);

        return repository.findAll();
    }

    @GetMapping("/populate")
    @ResponseBody
    public Map<String, String> populate() throws IOException {

        //try {
            return tpgs.populateDatabase();
        //}
        /*
        catch (IOException exc) {
            return Map.of(
                    "status", "error",
                    "message", exc.getMessage(),
                    "trace", Arrays.toString(exc.getStackTrace())
            );
        }
        */
    }


}
