package ru.griga.tickets.ms_frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.griga.tickets.shared.model.SearchParams;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String getIndex() {
        logger.info("Accessed /");
        return "frontpage/index";
    }

    @PostMapping("/search")
    public String getSearchPage(SearchParams searchParams) {
        logger.info("Accessed /search with searchParams=" + searchParams);
        return "search/search";
    }

}
