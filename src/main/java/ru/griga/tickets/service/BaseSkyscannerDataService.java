package ru.griga.tickets.service;

import kong.unirest.HttpRequest;
import kong.unirest.UnirestException;
import org.springframework.stereotype.Service;
import ru.griga.tickets.model.SearchParams;

@Service
public interface BaseSkyscannerDataService {

    Object fetchData(SearchParams searchParams) throws UnirestException;

}
