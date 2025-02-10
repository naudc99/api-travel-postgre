package es.project.apiagencia.config;

import es.project.apiagencia.entities.CountryEntity;
import es.project.apiagencia.repositories.CountryRepository;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Configuration
public class CountryConfig {

    private final CountryRepository countryRepository;

    public CountryConfig(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @PostConstruct
    public void initCountries() {
        createCountryIfNotFound("España");
        createCountryIfNotFound("Francia");
        createCountryIfNotFound("Italia");
        createCountryIfNotFound("Alemania");
        createCountryIfNotFound("Reino Unido");
        createCountryIfNotFound("Estados Unidos");
        createCountryIfNotFound("Canadá");
        createCountryIfNotFound("Australia");
        createCountryIfNotFound("Japón");
        createCountryIfNotFound("China");
    }

    @Transactional
    public void createCountryIfNotFound(String name) {
        CountryEntity country = countryRepository.findByName(name);
        if (country == null) {
            country = new CountryEntity(name);
            countryRepository.save(country);
        }
    }
}


