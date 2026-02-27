package org.truve.platform.ticketing.service.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.truve.platform.ticketing.service.domain.entity.MusicalScheduleSeat;
import org.truve.platform.ticketing.service.dto.AdmissionTokenClaimsDTO;
import org.truve.platform.ticketing.service.dto.SessionTicketValueDTO;
import org.truve.platform.ticketing.service.dto.TicketingResponse;
import org.truve.platform.ticketing.service.config.TicketingProperties;
import org.truve.platform.ticketing.service.jwt.AdmissionTokenService;
import org.truve.platform.ticketing.service.repository.MusicalScheduleSeatRepository;
import org.truve.platform.ticketing.service.repository.TicketingRedisRepository;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.Preconditions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketingService {

	private final TicketingRedisRepository ticketingRedisRepository;
	private final AdmissionTokenService admissionTokenService;
	private final TicketingProperties ticketingProperties;
	private final MusicalScheduleSeatRepository musicalScheduleSeatRepository;

	public TicketingResponse.Enter enter(Long musicalScheduleId, Long userId, String admissionToken) {
		AdmissionTokenClaimsDTO claims = admissionTokenService.parseAdmissionToken(admissionToken, musicalScheduleId, userId);

		boolean consumedAdmissionToken = ticketingRedisRepository.consumeAdmissionToken(claims.getShowId(), claims.getUserId(), admissionToken);
		Preconditions.validate(consumedAdmissionToken, ErrorCode.INVALID_ADMISSION_TOKEN);

		String sessionToken = UUID.randomUUID().toString();

		// TODO: 만료시간 기획측과 논의
		ticketingRedisRepository.saveSessionToken(sessionToken, userId, musicalScheduleId, Duration.ofMinutes(5));
		ticketingRedisRepository.addActiveTicketingUser(musicalScheduleId, sessionToken);
		long sessionTokenTtl = ticketingRedisRepository.getSessionTokenTtl(sessionToken);

		return new TicketingResponse.Enter(sessionToken, sessionTokenTtl);
	}

	public void heartbeat(Long musicalScheduleId, Long userId, String sessionToken) {

		isCorrectSessionToken(musicalScheduleId, userId, sessionToken);

		ticketingRedisRepository.addActiveTicketingUser(musicalScheduleId, sessionToken);
		long nowMs = System.currentTimeMillis();
		long activeWindowMs = ticketingProperties.getActiveWindowMs();
		ticketingRedisRepository.removeInactiveTicketingUsers(musicalScheduleId, nowMs - activeWindowMs);

		boolean extended = ticketingRedisRepository.refreshSessionTokenTtl(sessionToken, ticketingProperties.getSessionTtlSec());
		Preconditions.validate(extended, ErrorCode.INVALID_SESSION_TOKEN);
	}

	public void holdSeat(Long musicalScheduleId, Long userId, String sessionToken, Long musicalScheduleSeatId) {
		heartbeat(musicalScheduleId, userId, sessionToken);

		MusicalScheduleSeat seat = musicalScheduleSeatRepository.findById(musicalScheduleSeatId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_CORRECT_SEAT));

		Preconditions.validate(
			seat.isAvailable(),
			ErrorCode.ALREADY_SOLD_SEAT
		);

		Preconditions.validate(
			seat.getMusicalScheduleId().equals(musicalScheduleId),
			ErrorCode.NOT_CORRECT_SEAT
		);

		boolean tryHoldSeatResult = ticketingRedisRepository.tryHoldSeat(musicalScheduleId, seat.getSeatId(), sessionToken);
		Preconditions.validate(tryHoldSeatResult, ErrorCode.ALREADY_HOLD_SEAT);
	}

	private void isCorrectSessionToken(Long musicalScheduleId, Long userId, String sessionToken) {
		Preconditions.validate(sessionToken != null && !sessionToken.isBlank(), ErrorCode.INVALID_SESSION_TOKEN);

		SessionTicketValueDTO sessionValue = ticketingRedisRepository.getSessionTokenValue(sessionToken);

		Preconditions.validate(sessionValue != null, ErrorCode.INVALID_SESSION_TOKEN);
		Preconditions.validate(userId.equals(sessionValue.getUserId()), ErrorCode.SESSION_TOKEN_MISMATCH);
		Preconditions.validate(musicalScheduleId.equals(sessionValue.getShowId()), ErrorCode.SESSION_TOKEN_MISMATCH);
	}

}
