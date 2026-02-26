package com.truve.platform.musical.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truve.platform.musical.service.domain.entity.MusicalSeatPrice;

public interface MusicalSeatPriceRepository extends JpaRepository<MusicalSeatPrice, Long> {

	@Query("""
		select p
		from MusicalSeatPrice p
		where p.musical.id = :musicalId
		order by p.seatGrade asc
		""")
	List<MusicalSeatPrice> findSeatPrices(@Param("musicalId") Long musicalId);
}
