package ru.griga.tickets.ms_frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.griga.tickets.shared.model.SearchParams;

import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String getIndex() {
        return "frontpage/index";
    }

    @PostMapping(
            path = "/search")
    public String getSearchPage(SearchParams rBody) {
        logger.debug(rBody.toString());
        return "search/search";
    }

}
