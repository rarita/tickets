package ru.griga.tickets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.griga.tickets.repository.TravelRepository;

@Controller
@RequestMapping("/")
public class MainController {

    private final TravelRepository repository;

    MainController(@Autowired TravelRepository repository) {
        this.repository = repository;
    }

}
