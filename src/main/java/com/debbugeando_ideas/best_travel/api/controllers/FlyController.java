package com.debbugeando_ideas.best_travel.api.controllers;

import com.debbugeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.IFlyService;
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
@RequestMapping(path = "fly")
@AllArgsConstructor
@Slf4j
@Tag(name = "Fly")
public class FlyController {

    private final IFlyService flyService;

    @Operation(summary = "Get all ")
    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType){

        if(Objects.isNull(sortType)) sortType = SortType.NONE;
        var respose = this.flyService.readAll(page, size, sortType);
        return respose.isEmpty() ? ResponseEntity.noContent().build(): ResponseEntity.ok(respose);
    }

    @Operation(summary = "Get fly filter for origin and destiny")
    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(
            @RequestParam String origin,
            @RequestParam String destiny){
        var response = this.flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Get fly filter for less than price")
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice (
            @RequestParam BigDecimal price){
        var response = this.flyService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Get fly filter between price")
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice (
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max){
        var response = this.flyService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
