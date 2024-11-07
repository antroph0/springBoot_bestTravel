package com.debbugeando_ideas.best_travel.api.controllers;


import com.debbugeando_ideas.best_travel.api.models.responses.HotelResponse;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.IHotelService;
import com.debbugeando_ideas.best_travel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
@Slf4j
@Tag(name = "Hotel")
public class HotelController {

    private final IHotelService hotelService;

    @Operation(summary = "Get all hotels saved on BD")
    @GetMapping
    public ResponseEntity<Page<HotelResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false)SortType sortType){
        sortType = (Objects.isNull(sortType)) ? SortType.NONE : sortType;
        var response = this.hotelService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Get hotels filter for price less than")
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(
            @RequestParam BigDecimal price){
        var response = this.hotelService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Get hotels filter for price between")
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max){
        var response = this.hotelService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Get hotels from rating ")
    @GetMapping(path = "rating")
    public ResponseEntity<Set<HotelResponse>> getGreaterThan(
            @RequestParam Integer rating){

        if(rating > 4) rating = 4;
        if(rating < 1) rating = 1;
        var response = this.hotelService.readGreaterThan(rating);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
