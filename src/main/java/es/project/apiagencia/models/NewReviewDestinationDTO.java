package es.project.apiagencia.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewReviewDestinationDTO {
    
    private Integer rating;
    private String comment;
    private Date date;
    private Long destinationId;
    private Long userId;
}
 