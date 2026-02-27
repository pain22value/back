package com.truve.platform.performance.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.performance.service.domain.entity.PerformanceSeatGrade;

public interface PerformanceSeatGradeRepository extends JpaRepository<PerformanceSeatGrade, Long> {

	@Query("""
		select p
		from PerformanceSeatGrade p
		where p.performance.id = :performanceId
		order by p.basePrice desc
		""")
	List<PerformanceSeatGrade> findSeatPrices(@Param("performanceId") Long performanceId);
}
