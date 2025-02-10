package es.project.apiagencia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.project.apiagencia.entities.ReservationEntity;
import es.project.apiagencia.entities.UserEntity;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByUser(UserEntity user);
}
