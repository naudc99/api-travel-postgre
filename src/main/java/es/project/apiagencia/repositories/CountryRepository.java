package es.project.apiagencia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.project.apiagencia.entities.CountryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    CountryEntity findByName(String name);
}

