package org.truve.platform.ticketing.service.ticketing.service;

import org.springframework.stereotype.Service;
import org.truve.platform.ticketing.service.ticketing.repository.ScheduledSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketingInternalService {

	private final ScheduledSeatRepository scheduledSeatRepository;

	public void getRemainingSeats(Long showScheduleId) {

	}

}
