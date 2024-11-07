package com.debbugeando_ideas.best_travel.api.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketResponse implements Serializable {

    private UUID id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYY HH:mm")
    private LocalDateTime departureDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYY HH:mm")
    private LocalDateTime arrivalDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYY")
    private LocalDateTime purchaseDate;
    private BigDecimal price;
    private FlyResponse fly;

}
