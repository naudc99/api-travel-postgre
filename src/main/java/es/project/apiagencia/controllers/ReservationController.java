package es.project.apiagencia.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.project.apiagencia.models.DestinationDTO;
import es.project.apiagencia.models.NewReservationDTO;
import es.project.apiagencia.models.ReservationDTO;
import es.project.apiagencia.models.ResponseDTO;
import es.project.apiagencia.services.ReservationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody @Valid NewReservationDTO reservationNew, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        return reservationService.addReservation(reservationNew);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<List<ReservationDTO>> getAllReservationsByUserId(@PathVariable Long userId) {
        return reservationService.getAllReservationsByUserId(userId);
    }

    @PatchMapping("/{reservationId}/status")
    public ResponseEntity<ReservationDTO> updateReservationStatusToPaid(@PathVariable Long reservationId) {
        return reservationService.updateReservationStatusToPaid(reservationId);
    }

    @GetMapping("/{reservationId}/destinations")
    public ResponseEntity<List<DestinationDTO>> getDestinationsByReservationId(@PathVariable Long reservationId) {
        return reservationService.getDestinationsByReservationId(reservationId);
    }

    @DeleteMapping("/{reservationId}/destinations/{destinationId}")
    public ResponseEntity<ResponseDTO> deleteDestinationFromReservation(@PathVariable Long reservationId, @PathVariable Long destinationId) {
        return reservationService.deleteDestinationFromReservation(reservationId, destinationId);
    }

    @PostMapping("/{reservationId}/addDestination/{destinationId}")
    public ResponseEntity<ResponseDTO> addDestinationToReservation(@PathVariable Long reservationId, @PathVariable Long destinationId) {
        return reservationService.addDestinationToReservation(reservationId, destinationId);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ResponseDTO> deleteReservation(@PathVariable Long reservationId) {
        return reservationService.deleteReservation(reservationId);
    }
}