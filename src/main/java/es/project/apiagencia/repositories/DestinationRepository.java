package es.project.apiagencia.repositories;

import es.project.apiagencia.entities.DestinationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {
    Optional<DestinationEntity> findByName(String name);
}

