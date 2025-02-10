package es.project.apiagencia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.project.apiagencia.models.ReservationDTO;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destination_id")
    private DestinationEntity destination;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private AccommodationEntity accommodation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private ActivityEntity activity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "reservation_date", nullable = false, updatable = false)
    private Date reservationDate;

    @Column(name = "initial_date")
    private Date initialDate;

    @Column(name = "final_date")
    private Date finalDate;

    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name="price", nullable = false)
    private Double price;
    
    public ReservationDTO toDTO() {
        return new ReservationDTO(this);
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        if (initialDate == null || initialDate.before(now)) {
            throw new IllegalArgumentException("La fecha inicial debe ser posterior a la fecha actual");
        }
        if (finalDate == null || finalDate.before(initialDate)) {
            throw new IllegalArgumentException("La fecha final debe ser posterior a la fecha inicial");
        }
        if (passengerCount == null || passengerCount < 1) {
            throw new IllegalArgumentException("El número de pasajeros debe ser al menos 1");
        }
    }
}
