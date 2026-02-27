package com.truve.platform.performance.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.performance.service.domain.entity.PerformanceScheduleCasting;

public interface PerformanceScheduleCastingRepository extends JpaRepository<PerformanceScheduleCasting, Long> {

	@Query("""
		select sc
		from PerformanceScheduleCasting sc
		join fetch sc.performanceSchedule s
		join fetch sc.performanceCasting c
		join fetch c.artist
		where s.id in :scheduleIds
		""")
	List<PerformanceScheduleCasting> findAllByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);
}
