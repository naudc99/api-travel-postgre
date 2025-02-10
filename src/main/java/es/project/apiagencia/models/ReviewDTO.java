package es.project.apiagencia.models;

import es.project.apiagencia.entities.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    private Long reviewId;
    private Integer rating;
    private String comment;
    private Date date;
    private Long userId;
    private String userName;
    private Long destinationId;

    public ReviewDTO(ReviewEntity review) {
        this.reviewId = review.getReviewId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.date = review.getDate();
        this.userId= review.getReviewer() !=null ? review.getReviewer().getId() : null;
        this.userName = review.getReviewer() !=null ? review.getReviewer().getName() : null;
        this.destinationId = review.getDestination() != null ? review.getDestination().getDestinationId() : null;
    }
}
