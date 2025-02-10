package es.project.apiagencia.models;

import es.project.apiagencia.entities.RoleEntity;
import es.project.apiagencia.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDTO {

    private Long userId;
    private String name;
    private String email;
    private RoleEntity role;
    private List<ReservationDTO> reservations;
    private List<ReviewDTO> reviews;

    public UserRolesDTO(UserEntity user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        
        this.reservations = user.getReservations().stream()
                                .map(ReservationDTO::new)
                                .collect(Collectors.toList());
                                
        this.reviews = user.getReviews().stream()
                           .map(ReviewDTO::new)
                           .collect(Collectors.toList());
    }
}

