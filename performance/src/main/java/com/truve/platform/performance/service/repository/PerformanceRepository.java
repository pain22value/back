package com.truve.platform.performance.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.performance.service.domain.entity.Performance;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

	default Performance findByIdOrThrow(Long performanceId) {
		return findById(performanceId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_PERFORMANCE)
		);
	}
}
