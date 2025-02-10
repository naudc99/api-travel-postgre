package es.project.apiagencia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.project.apiagencia.entities.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
