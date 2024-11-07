package com.debbugeando_ideas.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Entity(name = "tour")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private Set<ReservationEntity> reservation;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "tour"
    )
    private Set<TicketEntity> tickets;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    @PrePersist
    @PreRemove
    public void updateFK(){
        this.tickets.forEach(ticket -> ticket.setTour(this));
        this.reservation.forEach(r -> r.setTour(this));
    }

    public void removeTicket(UUID id){
        this.tickets.forEach(ticket -> {
            if(ticket.getId().equals(id)){
                ticket.setTour(null);
            }
        });
    }

    public void addTicket(TicketEntity ticket){
        if(Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
        this.tickets.add(ticket);
        this.tickets.forEach(t -> t.setTour(this));
    }

    public void removeReservation(UUID id){
        this.reservation.forEach(r ->{
            if(r.getId().equals(id)){
                r.setTour(null);
            }
        });
    }

    public void addReservation(ReservationEntity reservation){
        if(Objects.isNull(reservation)) this.reservation = new HashSet<>();
        this.reservation.add(reservation);
        this.reservation.forEach( r -> r.setTour(this));
    }
}
