package com.truve.platform.musical.seat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truve.platform.musical.seat.domain.entity.SeatSection;

public interface SeatSectionRepository extends JpaRepository<SeatSection, Long> {
}
