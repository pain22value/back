package org.truve.platform.ticketing.service.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.truve.platform.ticketing.service.schedule.domain.entity.ScheduledSeat;

public interface ScheduledSeatRepository extends JpaRepository<ScheduledSeat, Long> {


}
