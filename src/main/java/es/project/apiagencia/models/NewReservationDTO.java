package es.project.apiagencia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewReservationDTO {
    private Long userId;
    private Long destinationId;
    private Long accommodationId;
    private Long activityId;
    private Date initialDate;
    private Date finalDate;
    private Integer passengerCount;
}
