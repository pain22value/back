package org.truve.platform.ticketing.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.truve.platform.ticketing.service.domain.entity.MusicalScheduleSeat;

public interface MusicalScheduleSeatRepository extends JpaRepository<MusicalScheduleSeat, Long> {
}
