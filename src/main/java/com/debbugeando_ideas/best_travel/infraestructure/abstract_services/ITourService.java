package com.debbugeando_ideas.best_travel.infraestructure.abstract_services;

import com.debbugeando_ideas.best_travel.api.models.requests.TourRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.TourResponse;
import lombok.extern.java.Log;

import java.util.UUID;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {

    void removeTicket( Long tourId, UUID ticketId);

    UUID addTicket(Long tourId, Long flyId);

    void removeReservation(Long tourId, UUID reservationId);

    UUID addReservation(Long tourId, Long hotelId, Integer totalDays);


}
