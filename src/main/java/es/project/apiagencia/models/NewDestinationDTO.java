package es.project.apiagencia.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewDestinationDTO {

    private Long destinationId;
    private String name;
    private Long countryId; 
    private String description;
    private double price;
    private byte[] image;
}

