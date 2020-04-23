package ru.griga.tickets.ms_gatherer.service;

import kong.unirest.UnirestException;
import org.springframework.stereotype.Service;
import ru.griga.tickets.shared.model.SearchParams;

import java.io.IOException;

@Service
public interface BaseDataService {

    Object fetchData(SearchParams searchParams) throws UnirestException, IOException;

}
