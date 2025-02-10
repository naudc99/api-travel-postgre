package es.project.apiagencia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import es.project.apiagencia.models.ReviewDTO;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @JsonIgnore 
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity reviewer;

    @ManyToOne
    @JoinColumn(name = "destination_id", referencedColumnName = "destination_id", nullable = true)
    private DestinationEntity destination;

    @ManyToOne
    @JoinColumn(name = "accommodation_id", referencedColumnName = "accommodation_id", nullable = true)
    private AccommodationEntity accommodation;

    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id", nullable = true)
    private ActivityEntity activity;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date")
    private Date date;

    public ReviewDTO toDTO(){
        return new ReviewDTO(this);
    }
}
