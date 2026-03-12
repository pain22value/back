package com.truve.platform.musical.review.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truve.platform.musical.review.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	boolean existsByUserIdAndShowId(UUID userId,  Long showId);

	List<Review> findByShowIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long showId);
	List<Review> findByShowIdAndDeletedAtIsNullAndIsPositiveTrueOrderByCreatedAtDesc(Long showId);
	List<Review> findByShowIdAndDeletedAtIsNullAndIsPositiveFalseOrderByCreatedAtDesc(Long showId);

	long countByShowIdAndDeletedAtIsNull(Long showId);
	long countByShowIdAndDeletedAtIsNullAndIsPositiveTrue(Long showId);


}
