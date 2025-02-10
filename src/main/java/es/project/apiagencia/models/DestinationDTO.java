package es.project.apiagencia.models;

import java.util.ArrayList;
import java.util.List;

import es.project.apiagencia.entities.DestinationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDTO {

    private Long destinationId;
    private String name;
    private String country; 
    private String description;
    private byte[] image;
    private double price;
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<ReservationDTO> reservations = new ArrayList<>();


    public DestinationDTO(DestinationEntity destination) {
        destinationId = destination.getDestinationId();
        name = destination.getName();
        country = destination.getCountry().getName(); 
        description = destination.getDescription();
        image = destination.getImage();
        price= destination.getPrice();
    }
}
