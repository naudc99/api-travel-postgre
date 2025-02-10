package es.project.apiagencia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.project.apiagencia.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByName(String name);

    @Query("SELECT u FROM UserEntity u WHERE TIMESTAMPDIFF(SECOND, u.lifeSpan, CURRENT_TIMESTAMP) > :threshold")
    Optional<UserEntity> findFirstUpdatable(int threshold);

    Optional<UserEntity> findById(Long id);
}

