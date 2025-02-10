package es.project.apiagencia.controllers;

import es.project.apiagencia.entities.CountryEntity;
import es.project.apiagencia.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryEntity>> getAllCountries() {
        List<CountryEntity> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryEntity> getCountryById(@PathVariable Long id) {
        Optional<CountryEntity> country = countryService.getCountryById(id);
        return country.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
