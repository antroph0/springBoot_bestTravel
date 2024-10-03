package com.debbugeando_ideas.best_travel.infraestructure.abstract_services;

import com.debbugeando_ideas.best_travel.api.models.requests.TicketRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.TicketResponse;

import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID>{
}
