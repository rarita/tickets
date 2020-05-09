package ru.griga.tickets.ms_frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.griga.tickets.shared.model.SearchParams;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("E, dd MMM yyyy", new Locale("ru", "RU"));

    @GetMapping("/")
    public String getIndex() {
        logger.info("Accessed /");
        return "frontpage/index";
    }

    @PostMapping("/search")
    public ModelAndView getSearchPage(SearchParams searchParams, String src, String dst) {
        logger.info("Accessed /search with searchParams=" + searchParams);
        ModelAndView mav = new ModelAndView("search/search");
        mav.addObject("searchParams", searchParams);

        // Сформировать и закинуть строку даты - статуса
        // чт, 14 мая, 1 взрослый
        // E, dd MMM yyyy
        String status = dtf.format(searchParams.getOutboundDateFrom()) + ", " + searchParams.getAdultsCount() + " взрослый";
        mav.addObject("statusString", status);

        mav.addObject("src", src);
        mav.addObject("dst", dst);

        return mav;
    }

}
