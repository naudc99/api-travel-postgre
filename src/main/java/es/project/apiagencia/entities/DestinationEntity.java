package es.project.apiagencia.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.project.apiagencia.models.DestinationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "destinations")
public class DestinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Long destinationId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private Double price;

    @Lob
    @Column(name = "image", length = 10485760) // 10 MB
    private byte[] image;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private CountryEntity country;

    @JsonIgnore
    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews;

    @JsonIgnore
    @OneToMany(mappedBy = "destination")
    private List<ReservationEntity> reservations;

    public DestinationDTO toDTO() {
        return new DestinationDTO(this);
    }
}
