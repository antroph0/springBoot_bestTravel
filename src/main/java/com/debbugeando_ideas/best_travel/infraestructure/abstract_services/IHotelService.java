package com.debbugeando_ideas.best_travel.infraestructure.abstract_services;

import com.debbugeando_ideas.best_travel.api.models.responses.HotelResponse;

import java.util.Set;

public interface IHotelService extends CatalogService<HotelResponse> {

    Set<HotelResponse> readGreaterThan(Integer rating);
}
