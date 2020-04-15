package ru.griga.tickets.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kong.unirest.UnirestException;
import org.springframework.stereotype.Service;
import ru.griga.tickets.model.SearchParams;

import java.io.IOException;

@Service
public interface BaseDataService {

    Object fetchData(SearchParams searchParams) throws UnirestException, IOException;

}
