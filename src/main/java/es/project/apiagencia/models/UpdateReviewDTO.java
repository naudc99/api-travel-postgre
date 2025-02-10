package es.project.apiagencia.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewDTO {
    private Long userId;
    private Long destinationId;
    private Integer rating;
    private String comment;
}
