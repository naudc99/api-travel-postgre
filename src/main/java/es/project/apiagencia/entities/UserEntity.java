package es.project.apiagencia.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.project.apiagencia.models.UserDTO;
import es.project.apiagencia.models.UserRolesDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = { "reviews", "reservations", "role" })
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    private String image = "";

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9]"
            + "(?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", message = "Email no válido")
    private String email;

    private String password;

    LocalDateTime lifeSpan = LocalDateTime.now();

    @JsonIgnore // Evita la serialización de esta relación para romper la referencia circular
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewEntity> reviews;

    @JsonIgnore // Evita la serialización de esta relación para romper la referencia circular
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservationEntity> reservations;


    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleEntity role;

    public UserDTO toDTO() {
        return new UserDTO(this);
    }

    public UserRolesDTO toRolesDTO() { return new UserRolesDTO(this); }
}
