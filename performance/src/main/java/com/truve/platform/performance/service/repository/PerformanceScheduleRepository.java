package com.truve.platform.performance.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.performance.service.domain.entity.PerformanceSchedule;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {

	@Query("""
		select s
		from PerformanceSchedule s
		where s.performance.id = :performanceId
		order by s.performanceTime asc
		""")
	List<PerformanceSchedule> findSchedules(@Param("performanceId") Long performanceId);
}
