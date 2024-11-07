package com.debbugeando_ideas.best_travel.infraestructure.services;

import com.debbugeando_ideas.best_travel.api.models.requests.ReservationRequest;
import com.debbugeando_ideas.best_travel.api.models.responses.HotelResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.ReservationResponse;
import com.debbugeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debbugeando_ideas.best_travel.domain.entities.ReservationEntity;
import com.debbugeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debbugeando_ideas.best_travel.domain.repositories.HotelRepository;
import com.debbugeando_ideas.best_travel.domain.repositories.ReservationRepository;
import com.debbugeando_ideas.best_travel.infraestructure.abstract_services.IReservationService;
import com.debbugeando_ideas.best_travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.debbugeando_ideas.best_travel.infraestructure.helpers.BlackListHelper;
import com.debbugeando_ideas.best_travel.infraestructure.helpers.CustomerHelper;
import com.debbugeando_ideas.best_travel.util.BestTravelUtil;
import com.debbugeando_ideas.best_travel.util.enums.Tables;
import com.debbugeando_ideas.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@AllArgsConstructor
public class ReservationService implements IReservationService {

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper apiCurrencyConnectorHelper;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        //create reservation
        var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .hotel(hotel)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .totalDays(request.getTotalDays())
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)))
                .build();
        this.customerHelper.incrase(customer.getDni(), ReservationService.class);

        var reservationPersisted = this.reservationRepository.save(reservationToPersist);

        log.info("Reservation saved with ID: " + reservationPersisted.getId());

        return this.entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID id) {

        var reservationFromDB = this.reservationRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.reservation.name()));
        return this.entityToResponse(reservationFromDB);

    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID id) {

        var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        var hotel = hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)));

        log.info("Reservation updated with id {}", reservationToUpdate.getId());
        var reservationUpdated = reservationRepository.save(reservationToUpdate);
        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID id) {
        var reserveationToDelete = reservationRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));
        this.customerHelper.decrase(reserveationToDelete.getCustomer().getDni(), ReservationService.class);
        this.reservationRepository.delete(reserveationToDelete);

    }

    @Override
    public BigDecimal findPrice(Long hotelId, Currency currency) {

        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var priceInDollars = hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage));

        if(currency.equals(Currency.getInstance("USD"))) return priceInDollars;
        var currencyDTO = this.apiCurrencyConnectorHelper.getCurrency(currency);
        log.info("API currency in {}, value response: {}", currencyDTO.getExchangeDate().toString(), currencyDTO.getRates());

        return priceInDollars.multiply(currencyDTO.getRates().get(currency));

    }

    private ReservationResponse entityToResponse(ReservationEntity entity){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);

        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);

        return response;
    }

    public static final BigDecimal charges_price_percentage = BigDecimal.valueOf(0.25);

    //Customized methods

}



