package com.truve.platform.musical.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.musical.service.domain.entity.MusicalSchedule;

public interface MusicalScheduleRepository extends JpaRepository<MusicalSchedule, Long> {

	@Query("""
		select s
		from MusicalSchedule s
		where s.musical.id = :musicalId
		order by s.dateTime asc
		""")
	List<MusicalSchedule> findSchedules(@Param("musicalId") Long musicalId);
}
