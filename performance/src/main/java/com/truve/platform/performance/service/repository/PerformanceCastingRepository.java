package com.truve.platform.performance.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.performance.service.domain.entity.PerformanceCasting;

public interface PerformanceCastingRepository extends JpaRepository<PerformanceCasting, Long> {

	@Query("""
		select a
		from PerformanceCasting a
		where a.performanceSchedule.id in :scheduleIds
		""")
	List<PerformanceCasting> findCastingsByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);
}
