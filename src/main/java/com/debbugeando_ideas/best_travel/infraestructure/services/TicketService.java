package com.debbugeando_ideas.best_travel.infraestructure.services;

import com.debbugeando_ideas.best_travel.api.models.requests.TicketRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debbugeando_ideas.best_travel.domain.entities.TicketEntity;
import com.debbugeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debbugeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debbugeando_ideas.best_travel.domain.repositories.TicketRepository;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.ITicketService;
import com.debbugeando_ideas.best_travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.debbugeando_ideas.best_travel.infraestructure.helpers.BlackListHelper;
import com.debbugeando_ideas.best_travel.infraestructure.helpers.CustomerHelper;
import com.debbugeando_ideas.best_travel.util.BestTravelUtil;
import com.debbugeando_ideas.best_travel.util.enums.Tables;
import com.debbugeando_ideas.best_travel.util.exceptions.IdNotFoundException;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor //Esta anotacion nos evita generar los constructores de los repositorios para injectar la dependencia, lombok lo hace automaticamente
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper apiCurrencyConnectorHelper;


    @Override
    public TicketResponse create(TicketRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));
        //Creando ticket
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .build();

        this.customerHelper.incrase(customer.getDni(), TicketService.class);

        var ticketPersisted = this.ticketRepository.save(ticketToPersist);

        log.info("Ticket saved with id: {}", ticketPersisted.getId());

        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID id) {
        var ticketFromDB = this.ticketRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID id) {
        var ticketToUpdate = ticketRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLatter());

        var ticketUpdated = ticketRepository.save(ticketToUpdate);
        log.info("Ticket updated with id: " + ticketUpdated.getId());
        return this.entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID id) {

        var ticketToDelete = ticketRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        this.customerHelper.decrase(ticketToDelete.getCustomer().getDni(), TicketService.class);
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId, Currency currency) {
        var fly = this.flyRepository.findById(flyId).orElseThrow(()-> new IdNotFoundException(Tables.ticket.name()));
        var priceInDollars = fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage));

        if(currency.equals(Currency.getInstance("USD"))) return priceInDollars;
        var currencyDTO = this.apiCurrencyConnectorHelper.getCurrency(currency);
        log.info("API currency in {}, value response: {}", currencyDTO.getExchangeDate().toString(), currencyDTO.getRates());

        return priceInDollars.multiply(currencyDTO.getRates().get(currency));
    }

    private TicketResponse entityToResponse(TicketEntity entity){
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);

        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);

        return response;
    }

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);


}
