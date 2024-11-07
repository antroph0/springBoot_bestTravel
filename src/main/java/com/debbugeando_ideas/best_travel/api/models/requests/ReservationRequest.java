package com.debbugeando_ideas.best_travel.api.models.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationRequest implements Serializable {

    @Size(min = 18, max = 20, message = "The size have to length between 18 and 20 characters")
    @NotBlank(message = "Id client is mandatory")
    public String idClient;

    @Positive
    @NotNull(message = "Id hotel is mandatory")
    public Long idHotel;

    @Min(value = 1, message = "Min one days to make reservation")
    @Max(value = 30, message = "Max 30 days to make reservation")
    @NotNull(message = "Total days is mandatory")
    public Integer totalDays;

    //@Pattern(regexp = "^(.+)@(.+)$")
    @Email
    private String email;
}
