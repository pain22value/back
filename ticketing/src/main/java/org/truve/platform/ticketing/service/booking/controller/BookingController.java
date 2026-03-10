package org.truve.platform.ticketing.service.booking.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.truve.platform.ticketing.service.booking.dto.BookingRequest;
import org.truve.platform.ticketing.service.booking.dto.BookingResponse;
import org.truve.platform.ticketing.service.booking.service.BookingService;

import com.truve.platform.common.response.ApiResult;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
	private static final String USER_ID_HEADER = "X-User-Id";

	private final BookingService bookingService;

	@Operation(summary = "예매 내역 생성", description = "예매 및 티켓 정보를 저장합니다.")
	@PostMapping
	public ApiResult<BookingResponse.Create> create(
		@RequestHeader(USER_ID_HEADER) Long userId,
		@RequestBody @Valid BookingRequest.Create request) {
		return ApiResult.ok(bookingService.create(userId, request));
	}

	@Operation(summary = "예매 결제 준비",
		description = "예매 상태를 결제 대기 중으로 변경하고, 결제 정보를 생성합니다. 결제 요청 전 호출해 주세요!")
	@PostMapping("/{reservationNumber}/payment")
	public ApiResult<Void> paymentProcess(
		@PathVariable String reservationNumber,
		@RequestBody @Valid BookingRequest.ApplicantInfo request
	) {
		bookingService.paymentProcess(reservationNumber, request);
		return ApiResult.ok();
	}
}
