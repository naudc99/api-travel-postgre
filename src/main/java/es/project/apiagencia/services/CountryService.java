package es.project.apiagencia.services;

import es.project.apiagencia.entities.CountryEntity;
import es.project.apiagencia.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<CountryEntity> getAllCountries() {
        return countryRepository.findAll();
    }

    public Optional<CountryEntity> getCountryById(Long id) {
        return countryRepository.findById(id);
    }
}

