package org.truve.platform.ticketing.service.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.truve.platform.ticketing.service.dto.TicketingResponse;
import org.truve.platform.ticketing.service.service.TicketingService;

import com.truve.platform.common.response.ApiResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketingController {

	private static final String USER_ID_HEADER = "X-User-Id";
	private static final String ADMISSION_HEADER = "X-Admission-Token";
	private static final String SESSION_HEADER = "X-Session-Ticket";

	private final TicketingService ticketingService;

	@PostMapping("/{musicalScheduleId}/enter")
	public ApiResult<TicketingResponse.Enter> enter(
		@PathVariable Long musicalScheduleId,
		@RequestHeader(value = USER_ID_HEADER) Long userId,
		@RequestHeader(value = ADMISSION_HEADER, required = false) String admissionToken
	) {
		var response = ticketingService.enter(musicalScheduleId, userId, admissionToken);
		return ApiResult.ok(response);
	}

	@PostMapping("/{musicalScheduleId}/heartbeat")
	public ApiResult<Void> heartbeat(
		@PathVariable Long musicalScheduleId,
		@RequestHeader(value = USER_ID_HEADER) Long userId,
		@RequestHeader(value = SESSION_HEADER) String sessionToken
	) {
		ticketingService.heartbeat(musicalScheduleId, userId, sessionToken);
		return ApiResult.ok();
	}

	@PostMapping("{musicalScheduleId}/hold/seat/{seatId}")
	public ApiResult<Void> holdSeat(
		@PathVariable Long seatId,
		@PathVariable Long musicalScheduleId,
		@RequestHeader(value = USER_ID_HEADER) Long userId,
		@RequestHeader(value = SESSION_HEADER) String sessionToken
	) {
		ticketingService.holdSeat(musicalScheduleId, userId, sessionToken, seatId);
		return ApiResult.ok();
	}

}
