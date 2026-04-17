package org.truve.platform.ticketing.service.booking.risk.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.truve.platform.ticketing.service.booking.risk.domain.entity.BookingBotRiskSummary;
import org.truve.platform.ticketing.service.booking.risk.dto.BeBotRiskReportRequest;
import org.truve.platform.ticketing.service.booking.risk.repository.BookingBotRiskSummaryRepository;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingBotRiskService {

	private final BookingBotRiskSummaryRepository bookingBotRiskSummaryRepository;

	@Transactional
	public void reportBeRisk(BeBotRiskReportRequest request) {
		if (!request.isBot() || request.getUserId() == null) {
			return;
		}

		BookingBotRiskSummary summary = bookingBotRiskSummaryRepository.findByUserId(request.getUserId())
			.orElseGet(() -> BookingBotRiskSummary.create(request.getUserId()));

		if (summary.isDuplicateOrder(request.getOrderId())) {
			return;
		}

		summary.applyBotDetection(request, LocalDateTime.now());
		bookingBotRiskSummaryRepository.save(summary);
	}

	@Transactional(readOnly = true)
	public void validatePaymentReady(UUID userId) {
		bookingBotRiskSummaryRepository.findByUserId(userId)
			.filter(summary -> summary.isBlocked(LocalDateTime.now()))
			.ifPresent(summary -> {
				throw new CustomException(ErrorCode.PAYMENT_RESTRICTED_BY_RISK);
			});
	}
}
