package es.project.apiagencia.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.project.apiagencia.entities.DestinationEntity;
import es.project.apiagencia.entities.ReviewEntity;
import es.project.apiagencia.entities.UserEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{

    List<ReviewEntity> findByReviewerId(Long userId);

    List<ReviewEntity> findByDestination(DestinationEntity destination);

    boolean existsByReviewerAndDestination(UserEntity reviewer, DestinationEntity destination);

    ReviewEntity findByReviewerIdAndDestinationDestinationId(Long userId, Long destinationId);

    ReviewEntity findByDestinationAndReviewer(DestinationEntity destination, UserEntity reviewer);
    
}
