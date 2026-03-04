package com.truve.platform.musical.seat.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truve.platform.musical.seat.domain.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
