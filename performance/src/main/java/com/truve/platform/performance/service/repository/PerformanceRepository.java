package com.truve.platform.performance.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.performance.service.domain.entity.Performance;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

	@Query("""
		select p
		from Performance p
		join fetch p.venue
		where p.id = :performanceId
		""")
	java.util.Optional<Performance> findDetailById(@Param("performanceId") Long performanceId);

	default Performance findByIdOrThrow(Long performanceId) {
		return findDetailById(performanceId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_PERFORMANCE)
		);
	}
}
