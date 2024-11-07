package com.debbugeando_ideas.best_travel.api.controllers;

import com.debbugeando_ideas.best_travel.api.models.requests.TicketRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.ErrorResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.ITicketService;
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
import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name = "Ticket")
public class TicketController {

    private final ITicketService ticketService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }
    )
    @Operation(summary = "Create a new ticket")
    @PostMapping
    public ResponseEntity<TicketResponse> post(@Valid @RequestBody TicketRequest request){
        return ResponseEntity.ok(ticketService.create(request));
    }

    @Operation(summary = "Get all ticket using the Id")
    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id){
        return ResponseEntity.ok(this.ticketService.read(id));
    }

    @Operation(summary = "Update the ticket information using the Id")
    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> update(@Valid @PathVariable UUID id , @RequestBody TicketRequest request){
        return ResponseEntity.ok(this.ticketService.update(request, id));
    }

    @Operation(summary = "Delete a ticket using the Id")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Busquedas filtradas
    @Operation (summary = "Get tickets using a filter for prices")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice (
            @RequestParam Long flyId,
            @RequestHeader(required = false)Currency currency){
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice(flyId, currency)));
    }
}
