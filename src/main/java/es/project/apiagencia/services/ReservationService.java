package es.project.apiagencia.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.project.apiagencia.entities.DestinationEntity;
import es.project.apiagencia.entities.ReservationEntity;
import es.project.apiagencia.entities.UserEntity;
import es.project.apiagencia.models.DestinationDTO;
import es.project.apiagencia.models.NewReservationDTO;
import es.project.apiagencia.models.ReservationDTO;
import es.project.apiagencia.models.ResponseDTO;
import es.project.apiagencia.repositories.DestinationRepository;
import es.project.apiagencia.repositories.ReservationRepository;
import es.project.apiagencia.repositories.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, DestinationRepository destinationRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
    }

    public ResponseEntity<ReservationDTO> addReservation(NewReservationDTO reservationNew) {
        try {
            Long userId = reservationNew.getUserId();
            if (userId == null) {
                return ResponseEntity.unprocessableEntity().build();
            }

            UserEntity user = userRepository.findById(userId).orElse(null);
            
            DestinationEntity destination = destinationRepository.findById(reservationNew.getDestinationId()).orElse(null);
    
            if (destination == null) {
                return ResponseEntity.unprocessableEntity().build();
            }
    
            ReservationEntity reservation = new ReservationEntity();
            reservation.setUser(user);
            reservation.setDestination(destination);
            reservation.setReservationDate(new Date());
            reservation.setInitialDate(reservationNew.getInitialDate());
            reservation.setFinalDate(reservationNew.getFinalDate());
            reservation.setPassengerCount(reservationNew.getPassengerCount());
            reservation.setStatus("pending"); 

            long diffInMillies = Math.abs(reservation.getFinalDate().getTime() - reservation.getInitialDate().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            double totalPrice = destination.getPrice() * reservation.getPassengerCount() * diffInDays;
            reservation.setPrice(totalPrice);
                
            reservation = reservationRepository.save(reservation);
            return ResponseEntity.ok(new ReservationDTO(reservation));
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.internalServerError().build();
        }
    }
    
    

    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        try {
            List<ReservationEntity> reservationList = reservationRepository.findAll();
            if (reservationList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<ReservationDTO> reservationDTOList = new ArrayList<>();
                for (ReservationEntity reservation : reservationList) {
                    reservationDTOList.add(new ReservationDTO(reservation));
                }
                return ResponseEntity.ok(reservationDTOList);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<ReservationDTO>> getAllReservationsByUserId(Long userId) {
        try {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            List<ReservationEntity> reservationList = reservationRepository.findByUser(user);
            if (reservationList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                List<ReservationDTO> reservationDTOList = new ArrayList<>();
                for (ReservationEntity reservation : reservationList) {
                    reservationDTOList.add(new ReservationDTO(reservation));
                }
                return ResponseEntity.ok(reservationDTOList);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<ReservationDTO> updateReservationStatusToPaid(Long reservationId) {
        try {
            ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            reservation.setStatus("Paid");
            reservation = reservationRepository.save(reservation);
            return ResponseEntity.ok(new ReservationDTO(reservation));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<DestinationDTO>> getDestinationsByReservationId(Long reservationId) {
        try {
            ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            DestinationEntity destination = reservation.getDestination();
            if (destination == null) {
                return ResponseEntity.noContent().build();
            }
            DestinationDTO destinationDTO = new DestinationDTO(destination);
            return ResponseEntity.ok(Collections.singletonList(destinationDTO));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    

    public ResponseEntity<ResponseDTO> deleteDestinationFromReservation(Long reservationId, Long destinationId) {
        try {
            ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            DestinationEntity destination = reservation.getDestination();
            if (destination == null || !destination.getDestinationId().equals(destinationId)) {
                return ResponseEntity.notFound().build();
            }
            reservation.setDestination(null);
            reservationRepository.save(reservation);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.newMessage("Destination removed from reservation successfully.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = new ResponseDTO("Error removing destination from reservation.");
            responseDTO.newError(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDTO);
        }
    }
    
    

    public ResponseEntity<ResponseDTO> addDestinationToReservation(Long reservationId, Long destinationId) {
        try {
            ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            DestinationEntity destination = destinationRepository.findById(destinationId).orElse(null);
            if (destination == null) {
                return ResponseEntity.notFound().build();
            }
            reservation.setDestination(destination);
            reservationRepository.save(reservation);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.newMessage("Destination added to reservation successfully.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<ResponseDTO> deleteReservation(Long reservationId) {
        try {
            ReservationEntity reservation = reservationRepository.findById(reservationId).orElse(null);
            if (reservation == null) {
                return ResponseEntity.notFound().build();
            }
            reservationRepository.delete(reservation);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.newMessage("Reservation deleted successfully.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = new ResponseDTO("Error deleting reservation.");
            responseDTO.newError(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDTO);
        }
    }
    
    
}