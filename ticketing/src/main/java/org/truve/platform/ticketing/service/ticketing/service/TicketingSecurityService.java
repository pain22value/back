package org.truve.platform.ticketing.service.ticketing.service;

import org.springframework.stereotype.Service;
import org.truve.platform.ticketing.service.ticketing.repository.TicketingRedisRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TicketingSecurityService {
	private final TicketingRedisRepository ticketingRedisRepository;

	public void findMacro(String sessionTicket) {
		ticketingRedisRepository.findMacro(sessionTicket);
	}
}
