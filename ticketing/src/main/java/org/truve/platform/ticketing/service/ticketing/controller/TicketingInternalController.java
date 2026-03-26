package org.truve.platform.ticketing.service.ticketing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.truve.platform.ticketing.service.ticketing.service.TicketingInternalService;

import com.truve.platform.common.response.ApiResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/ticketing")
public class TicketingInternalController {
	private final TicketingInternalService ticketingInternalService;

	@GetMapping("/{showScheduleId/remaining")
	public ApiResult<Void> getRemainingSeats(
		@PathVariable Long showScheduleId
	) {
		ticketingInternalService.getRemainingSeats(showScheduleId);
		return ApiResult.ok();
	}
}
