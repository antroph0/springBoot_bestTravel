package com.debbugeando_ideas.best_travel.api.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketRequest implements Serializable {

    @Size(min = 18, max = 20, message = "The size have to length between 18 and 20 characters")
    @NotBlank(message = "Id client is mandatory")
    private String idClient;

    @Positive
    @NotNull(message = "Id hotel is mandatory")
    private Long idFly;
}
