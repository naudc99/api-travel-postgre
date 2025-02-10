package es.project.apiagencia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import es.project.apiagencia.entities.ReservationEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long reservationId;
    private Long userId;
    private Long destinationId;
    private Long activityId;
    private Long accommodationId; 
    private Date reservationDate;
    private Date initialDate;
    private Date finalDate;
    private Integer passengerCount;
    private String status;
    private Double price;

    public ReservationDTO(ReservationEntity reservation) {
        this.reservationId = reservation.getReservationId();
        this.userId = reservation.getUser().getId();
        if (reservation.getDestination() != null) {
                this.destinationId = reservation.getDestination().getDestinationId();
        }
        if (reservation.getActivity() != null) {
                this.activityId = reservation.getActivity().getActivityId();
        }

        if (reservation.getAccommodation() != null) {
                this.accommodationId = reservation.getAccommodation().getAccommodationId();
        }

        this.reservationDate = reservation.getReservationDate();
        this.initialDate = reservation.getInitialDate();
        this.finalDate = reservation.getFinalDate();
        this.passengerCount = reservation.getPassengerCount();
        this.status = reservation.getStatus();
        this.price = reservation.getPrice();
    }
}
