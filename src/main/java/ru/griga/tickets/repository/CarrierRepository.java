package ru.griga.tickets.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ru.griga.tickets.model.Carrier;

public interface CarrierRepository extends Neo4jRepository<Carrier, String> {
}
