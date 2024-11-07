package com.debbugeando_ideas.best_travel.infraestructure.abstract_services;

import com.debbugeando_ideas.best_travel.api.models.requests.ReservationRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID>{

    BigDecimal findPrice(Long hotelId, Currency currency);
}
