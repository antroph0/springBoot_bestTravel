package com.debbugeando_ideas.best_travel.infraestructure.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDTO implements Serializable {

    @JsonProperty(value = "date")
    private LocalDate exchangeDate;

    private Map<Currency, BigDecimal> rates;
}
