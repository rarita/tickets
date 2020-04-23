package ru.griga.tickets.shared.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import ru.griga.tickets.shared.model.Carrier;

@Repository
public interface CarrierRepository extends Neo4jRepository<Carrier, String> {
}
