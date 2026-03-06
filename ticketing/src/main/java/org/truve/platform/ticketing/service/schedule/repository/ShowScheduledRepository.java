package org.truve.platform.ticketing.service.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.truve.platform.ticketing.service.schedule.domain.entity.ScheduledSeat;
import org.truve.platform.ticketing.service.schedule.domain.entity.ShowScheduled;

public interface ShowScheduledRepository extends JpaRepository<ShowScheduled, Long> {
}
