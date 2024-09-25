package com.debbugeando_ideas.best_travel.domain.repositories;

import com.debbugeando_ideas.best_travel.domain.entities.TicketEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {

}
