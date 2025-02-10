package es.project.apiagencia.models;

import es.project.apiagencia.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String name;
    private String email;
    private String image;
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<ReservationDTO> reservations = new ArrayList<>();

    public UserDTO(UserEntity user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.image = user.getImage();
        
        if (user.getReviews() != null && !user.getReviews().isEmpty())
            this.reviews = user.getReviews().stream().map(ReviewDTO::new).toList();
        
        if (user.getReservations() != null && !user.getReservations().isEmpty())
            this.reservations = user.getReservations().stream().map(ReservationDTO::new).toList();
    }
}


