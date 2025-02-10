package es.project.apiagencia.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.project.apiagencia.entities.DestinationEntity;
import es.project.apiagencia.entities.ReviewEntity;
import es.project.apiagencia.entities.UserEntity;
import es.project.apiagencia.models.NewReviewDestinationDTO;
import es.project.apiagencia.models.ResponseDTO;
import es.project.apiagencia.models.ReviewDTO;
import es.project.apiagencia.repositories.DestinationRepository;
import es.project.apiagencia.repositories.ReviewRepository;
import es.project.apiagencia.repositories.UserRepository;

@Service
public class ReviewService{

    private static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor";
    
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, DestinationRepository destinationRepository){
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
    }

    public List<ReviewDTO> getAllReviewsByDestinationId(Long destinationId) {
        DestinationEntity destination = destinationRepository.findById(destinationId).orElse(null);
        if (destination == null) {
            return null; // O manejar el caso de que el destino no exista de otra manera
        } else {
            List<ReviewEntity> reviews = reviewRepository.findByDestination(destination);
            return reviews.stream()
                .map(ReviewEntity::toDTO)
                .collect(Collectors.toList());
        }
    }
    
    public ResponseEntity<ReviewDTO> addDestinationReview(NewReviewDestinationDTO reviewDestinationNew) {
        ResponseDTO response = new ResponseDTO();
        try {
            Long userId = reviewDestinationNew.getUserId();
            Long destinationId = reviewDestinationNew.getDestinationId();
            UserEntity user = userRepository.findById(userId).orElse(null);
            DestinationEntity destination = destinationRepository.findById(destinationId).orElse(null);
            boolean hasScored = reviewRepository.existsByReviewerAndDestination(user, destination);
            if (hasScored)
                return ResponseEntity.badRequest().build();
            else if (user == null || destination == null)
                return ResponseEntity.unprocessableEntity().build();
            else {
                Integer value = reviewDestinationNew.getRating();
                if (value < 0 || value > 10)
                    return ResponseEntity.badRequest().build();
                ReviewEntity newReview = new ReviewEntity();
                newReview.setReviewer(user);
                newReview.setDestination(destination);
                newReview.setRating(value);
                newReview.setComment(reviewDestinationNew.getComment());
                LocalDate localDate = LocalDate.now();
                Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                newReview.setDate(date);
                newReview = reviewRepository.save(newReview);
                return ResponseEntity.ok(newReview.toDTO());
            }
        } catch (Exception e) {
            response.newError(INTERNAL_ERROR_MESSAGE);
            return ResponseEntity.internalServerError().build();
        }
    }

    // public ResponseEntity<PaginatedScore> getScoresByBookId(
    //         Long bookId,
    //         String sort,
    //         Sort.Direction order,
    //         int page,
    //         int perPage) {
    //     try {
    //         PageRequest pageable = PageRequest.of(page, perPage, order, sort);
    //         BookEntity book = destinationRepository.findById(bookId).orElse(null);
    //         if (book == null) {
    //             return ResponseEntity.unprocessableEntity().body(null);
    //         } else {
    //             Page<ScoreEntity> scorePage = reviewRepository.findByBookScores(book, pageable);
    //             if (scorePage.isEmpty()) {
    //                 return ResponseEntity.noContent().build();
    //             } else {
    //                 List<ScoreDTO> scoreDTOs = scorePage.getContent().stream()
    //                         .map(ScoreEntity::toDTO).toList();
    //                 PaginatedScore paginatedScore = new PaginatedScore();
    //                 paginatedScore.setItems(scoreDTOs);
    //                 paginatedScore.setTotalCount(scorePage.getTotalElements());
    //                 return ResponseEntity.ok(paginatedScore);
    //             }
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.internalServerError().build();
    //     }
    // }

    public ResponseEntity<List<ReviewDTO>> getReviewByDestinationIdAndUserId(Long destinationId, Long userId) {
        try {
            DestinationEntity destination = destinationRepository.findById(destinationId).orElse(null);
            UserEntity user = userRepository.findById(userId).orElse(null);
            
            if (destination == null || user == null) {
                return ResponseEntity.unprocessableEntity().body(null);
            } else {
                ReviewEntity review = reviewRepository.findByDestinationAndReviewer(destination, user);
                if (review == null) {
                    return ResponseEntity.noContent().build();
                } else {
                    ReviewDTO reviewDTO = review.toDTO();
                    return ResponseEntity.ok(Collections.singletonList(reviewDTO));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    public ResponseEntity<ResponseDTO> deleteReviewByUserIdAndDestinationId(Long userId, Long destinationId) {
        ResponseDTO response = new ResponseDTO();
        try {
            ReviewEntity review = reviewRepository.findByReviewerIdAndDestinationDestinationId(userId, destinationId);
            if (review == null) {
                response.newError("La valoración no existe.");
                return ResponseEntity.unprocessableEntity().body(response);
            } else {
                reviewRepository.delete(review);
                response.newMessage("La valoración ha sido eliminada correctamente.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.newError(INTERNAL_ERROR_MESSAGE);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    public ResponseEntity<ResponseDTO> updateReviewByUserIdAndDestinationId(Long userId, Long destinationId, Integer newValue, String newDescription) {
        ResponseDTO response = new ResponseDTO();
        try {
            ReviewEntity review = reviewRepository.findByReviewerIdAndDestinationDestinationId(userId, destinationId);
            if (review == null) {
                response.newError("La valoración no existe.");
                return ResponseEntity.unprocessableEntity().body(response);
            } else {
                if (newValue != null && (newValue < 0 || newValue > 10)) {
                    response.newError("El nuevo valor debe estar entre 0 y 10.");
                    return ResponseEntity.badRequest().body(response);
                }
                if (newValue != null) {
                    review.setRating(newValue);
                }
                if (newDescription != null) {
                    review.setComment(newDescription);
                }
                reviewRepository.save(review);
                response.newMessage("La valoración ha sido actualizada correctamente.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.newError(INTERNAL_ERROR_MESSAGE);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}