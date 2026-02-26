package com.truve.platform.musical.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.musical.service.domain.entity.MusicalActor;

public interface MusicalActorRepository extends JpaRepository<MusicalActor, Long> {

	@Query("""
		select a
		from MusicalActor a
		where a.schedule.id in :scheduleIds
		""")
	List<MusicalActor> findActorsByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);
}
