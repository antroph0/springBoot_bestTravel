package com.debbugeando_ideas.best_travel.domain.repositories;

import com.debbugeando_ideas.best_travel.domain.entities.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID> {

}
