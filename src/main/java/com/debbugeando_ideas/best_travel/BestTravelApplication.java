package com.debbugeando_ideas.best_travel;

import com.debbugeando_ideas.best_travel.domain.entities.ReservationEntity;
import com.debbugeando_ideas.best_travel.domain.entities.TicketEntity;
import com.debbugeando_ideas.best_travel.domain.entities.TourEntity;
import com.debbugeando_ideas.best_travel.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
@Slf4j

public class BestTravelApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(BestTravelApplication.class);
	private final CustomerRepository customerRepository;
	private final FlyRepository flyRepository;
	private final HotelRepository hotelRepository;
	private final ReservationRepository reservationRepository;
	private final TicketRepository ticketRepository;
	private final TourRepository tourRepository;

    public BestTravelApplication(
				CustomerRepository customerRepository,
				FlyRepository flyRepository,
				HotelRepository hotelRepository,
				ReservationRepository reservationRepository,
				TicketRepository ticketRepository,
				TourRepository tourRepository) {

        this.customerRepository = customerRepository;
        this.flyRepository = flyRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;
        this.ticketRepository = ticketRepository;
        this.tourRepository = tourRepository;
    }


    public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		var fly = flyRepository.findById(15L).get();
//		var hotel = hotelRepository.findById(7L).get();
//		var ticket = ticketRepository.findById(UUID.fromString("32345678-1234-5678-4234-567812345678")).get();
//		var reservation = reservationRepository.findById(UUID.fromString("32345678-1234-5678-1234-567812345678")).get();
//		var customer = customerRepository.findById("BBMB771012HMCRR022").get();
//
//
//		log.info("Vuelo: >>> " + String.valueOf(fly) + "Hotel: >>> " + String.valueOf(hotel) );
//		log.info("Su ticket corresponde a: >>> " + ticket);
//		log.info("Su reservacion corresponde a: >>> " + reservation);
//		log.info("Su usuario corresponde a: >>> " + customer);

		//this.flyRepository.selectLessPrice(BigDecimal.valueOf(20)).forEach(f -> System.out.println(f));
		//this.flyRepository.selectBetweenPrice(BigDecimal.valueOf(15), BigDecimal.valueOf(20)).forEach(f -> System.out.println(f));
		/*this.flyRepository.selectOriginDestiny("Canada", "Mexico").forEach(f -> System.out.println(f));

		//Una forma de hacer un join directo para obtener el vuelo por id
		var fly = flyRepository.findById(1L).get();
		fly.getTickets().forEach(ticket -> System.out.println(ticket));*/

		/*var fly = flyRepository.findByTicketId(UUID.fromString("12345678-1234-5678-2236-567812345678")).get();
		System.out.println(fly);

		//hotelRepository.findByPriceLessThan(BigDecimal.valueOf(100)).forEach(System.out::println);
		//hotelRepository.findByPriceIsBetween(BigDecimal.valueOf(100), BigDecimal.valueOf(200)).forEach(System.out::println);
		hotelRepository.findByRatingGreaterThan(4).forEach(System.out::println);
		var hotel = hotelRepository.findByReservationId(UUID.fromString("12345678-1234-5678-1234-567812345678")).get();
		System.out.println("Su reservacion corresponde al hotel: " + hotel);*/

		// Valores necesarios para crear un tour en la base de datos
		var customer = customerRepository.findById("GOTW771012HMRGR087").orElseThrow();
		log.info("Client name: " + customer.getFullName());

		var fly = flyRepository.findById(11L).orElseThrow();
		log.info("Vuelo: " + fly.getOriginName() + " - " + fly.getDestinyName());

		var hotel = hotelRepository.findById(3L).orElseThrow();
		log.info("Hotel: " + hotel.getName() + " - " + hotel.getAddress());

		var tour = TourEntity.builder()
				.customer(customer)
				.build();

		var ticket = TicketEntity.builder()
				.id(UUID.randomUUID())
				.price(fly.getPrice().multiply(BigDecimal.TEN))
				.arrivalDate(LocalDate.now())
				.departureDate(LocalDate.now())
				.purchaseDate(LocalDate.now())
				.customer(customer)
				.tour(tour)
				.fly(fly)
				.build();

		var reservation = ReservationEntity.builder()
				.id(UUID.randomUUID())
				.dateTimeReservation(LocalDateTime.now())
				.dateEnd(LocalDate.now().plusDays(2))
				.dateStart(LocalDate.now().plusDays(1))
				.hotel(hotel)
				.customer(customer)
				.tour(tour)
				.totalDays(1)
				.price(hotel.getPrice().multiply(BigDecimal.TEN))
				.build();

	}
}
