package es.project.apiagencia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.project.apiagencia.entities.CountryEntity;
import es.project.apiagencia.entities.DestinationEntity;
import es.project.apiagencia.models.NewDestinationDTO;
import es.project.apiagencia.services.DestinationService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/destination")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public ResponseEntity<List<DestinationEntity>> getAllDestinations() {
        List<DestinationEntity> destinations = destinationService.getAllDestinations();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationEntity> getDestinationById(@PathVariable Long id) {
        Optional<DestinationEntity> destination = destinationService.getDestinationById(id);
        return destination.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
public ResponseEntity<DestinationEntity> createDestinationWithImage(
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("countryId") Long countryId,
        @RequestParam("price") double price,
        @RequestParam("image") MultipartFile imageFile) throws IOException {
    
    DestinationEntity createdDestination = destinationService.createDestinationWithImage(name, description, countryId, price, imageFile);
    return ResponseEntity.status(201).body(createdDestination);
}


    @PutMapping("/{id}")
    public ResponseEntity<DestinationEntity> updateDestination(
            @PathVariable Long id,
            @RequestBody NewDestinationDTO updatedDestinationDTO) {
        Optional<DestinationEntity> updatedDestination = destinationService.updateDestination(id, updatedDestinationDTO);
        return updatedDestination.map(ResponseEntity::ok)
                                 .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        boolean deleted = destinationService.deleteDestination(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
