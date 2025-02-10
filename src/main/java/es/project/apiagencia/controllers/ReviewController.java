package es.project.apiagencia.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.project.apiagencia.models.NewReviewDestinationDTO;
import es.project.apiagencia.models.ResponseDTO;
import es.project.apiagencia.models.ReviewDTO;
import es.project.apiagencia.models.UpdateReviewDTO;
import es.project.apiagencia.services.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByDestinationId(@PathVariable Long destinationId) {
        List<ReviewDTO> reviews = reviewService.getAllReviewsByDestinationId(destinationId);
        if (reviews == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // O manejar el caso de destino no encontrado de otra manera
        } else if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // O manejar el caso de que no haya reseñas de otra manera
        } else {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
    }
    
    @PostMapping()
    public ResponseEntity<ReviewDTO> addDestinationReview(@RequestBody @Valid NewReviewDestinationDTO reviewNew) {
        return reviewService.addDestinationReview(reviewNew);
    }
    
    @DeleteMapping("/{userId}/{destinationId}")
    public ResponseEntity<ResponseDTO> deleteReviewByUserIdAndDestinationId(@PathVariable Long userId, @PathVariable Long destinationId) {
        return reviewService.deleteReviewByUserIdAndDestinationId(userId, destinationId);
    }

    @PatchMapping("/{userId}/{destinationId}")
    public ResponseEntity<ResponseDTO> updateReviewByUserIdAndDestinationId(
            @PathVariable Long userId,
            @PathVariable Long destinationId,
            @RequestBody UpdateReviewDTO updateReviewDTO) {
        return reviewService.updateReviewByUserIdAndDestinationId(userId, destinationId, updateReviewDTO.getRating(), updateReviewDTO.getComment());
    }

    @GetMapping("/{destinationId}/{userId}")
    public ResponseEntity<List<ReviewDTO>> getScoreByDestinationIdAndUserId(@PathVariable Long destinationId, @PathVariable Long userId) {
        return reviewService.getReviewByDestinationIdAndUserId(destinationId, userId);
    }
}
