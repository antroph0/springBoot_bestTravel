package com.debbugeando_ideas.best_travel.api.controllers;

import com.debbugeando_ideas.best_travel.api.models.requests.ReservationRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.ErrorResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.ReservationResponse;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
@Tag(name = "Reservation")
public class ReservationController {

    private final IReservationService reservationService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Create a reservation")
    @PostMapping
    public ResponseEntity<ReservationResponse> post(@Valid @RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.create(request));
    }

    @Operation(summary = "Get all reservation from DB")
    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> get(@PathVariable UUID id){
        return ResponseEntity.ok(this.reservationService.read(id));
    }

    @Operation(summary = "Update partially a reservation using ID")
    @PutMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> update(@Valid @PathVariable UUID id, @RequestBody ReservationRequest request){
        return ResponseEntity.ok(this.reservationService.update(request, id));
    }

    @Operation(summary = "Delete a reservation using Id")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete (@PathVariable UUID id){
        this.reservationService.delete(id);
        return  ResponseEntity.noContent().build();
    }

    @Operation(summary = "Return a reservation price given")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getReservationPrice(
            @RequestParam Long hotelId,
            @RequestHeader(required = false) Currency currency){

        if(Objects.isNull(currency)) currency = Currency.getInstance("USD");

        return ResponseEntity.ok(Collections.singletonMap("hotelPrice", this.reservationService.findPrice(hotelId, currency)));
    }
}
