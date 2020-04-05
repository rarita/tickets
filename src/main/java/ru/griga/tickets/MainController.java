package ru.griga.tickets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.griga.tickets.model.place.Airport;
import ru.griga.tickets.model.place.base.Place;
import ru.griga.tickets.repository.TravelRepository;


@Controller
@RequestMapping("/")
public class MainController {

    private final TravelRepository repository;

    MainController(@Autowired TravelRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/save")
    @ResponseBody
    public Iterable<Place> save() {
        var air = new Airport("LED", "Пулково", "Pulkovo", 0.1, 1.0);
        repository.save(air);

        return repository.findAll();
    }

}
