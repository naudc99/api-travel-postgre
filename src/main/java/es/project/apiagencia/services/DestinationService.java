package es.project.apiagencia.services;

import es.project.apiagencia.entities.CountryEntity;
import es.project.apiagencia.entities.DestinationEntity;
import es.project.apiagencia.models.NewDestinationDTO;
import es.project.apiagencia.repositories.CountryRepository;
import es.project.apiagencia.repositories.DestinationRepository;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;

    public List<DestinationEntity> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Optional<DestinationEntity> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public DestinationEntity createDestinationWithImage(String name, String description, Long countryId, double price, MultipartFile imageFile) throws IOException {
        NewDestinationDTO newDestinationDTO = new NewDestinationDTO();
        newDestinationDTO.setName(name);
        newDestinationDTO.setDescription(description);
        newDestinationDTO.setCountryId(countryId);
        newDestinationDTO.setPrice(price);

        DestinationEntity destination = convertToEntity(newDestinationDTO);

        if (imageFile != null && !imageFile.isEmpty()) {
            byte[] imageData = imageFile.getBytes();
            destination.setImage(imageData);
        }

        return destinationRepository.save(destination);
    }
    

    public Optional<DestinationEntity> updateDestination(Long id, NewDestinationDTO updatedDestinationDTO) {
        Optional<DestinationEntity> optionalDestination = destinationRepository.findById(id);
        if (optionalDestination.isPresent()) {
            DestinationEntity destination = optionalDestination.get();
            // Actualizar los campos del destino con los valores del DTO
            destination.setName(updatedDestinationDTO.getName());
            destination.setDescription(updatedDestinationDTO.getDescription());
            destination.setPrice(updatedDestinationDTO.getPrice());
            // Si el país también se ha actualizado
            if (updatedDestinationDTO.getCountryId() != null) {
                // Actualizar el país del destino
                Optional<CountryEntity> optionalCountry = countryService.getCountryById(updatedDestinationDTO.getCountryId());
                optionalCountry.ifPresent(country -> destination.setCountry(country));
            }
            // Si la imagen también se ha actualizado
            if (updatedDestinationDTO.getImage() != null) {
                destination.setImage(updatedDestinationDTO.getImage());
            }
            // Guardar la entidad actualizada en la base de datos
            return Optional.of(destinationRepository.save(destination));
        } else {
            return Optional.empty();
        }
    }
    public boolean deleteDestination(Long id) {
        if (destinationRepository.existsById(id)) {
            destinationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private DestinationEntity convertToEntity(NewDestinationDTO destinationDTO) {
        DestinationEntity destinationEntity = new DestinationEntity();
        destinationEntity.setName(destinationDTO.getName());
        destinationEntity.setDescription(destinationDTO.getDescription());
        destinationEntity.setPrice(destinationDTO.getPrice());

        Long countryId = destinationDTO.getCountryId();
        if (countryId != null) {
            Optional<CountryEntity> countryOptional = countryRepository.findById(countryId);
            countryOptional.ifPresent(destinationEntity::setCountry);
        }

        return destinationEntity;
    }
}


