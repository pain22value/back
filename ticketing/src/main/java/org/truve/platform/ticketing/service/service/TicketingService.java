package org.truve.platform.ticketing.service.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.truve.platform.ticketing.service.dto.AdmissionTokenClaimsDTO;
import org.truve.platform.ticketing.service.dto.TicketingResponse;
import org.truve.platform.ticketing.service.jwt.AdmissionTokenService;
import org.truve.platform.ticketing.service.repository.TicketingRedisRepository;

import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.Preconditions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketingService {

	private final TicketingRedisRepository ticketingRedisRepository;
	private final AdmissionTokenService admissionTokenService;

	public TicketingResponse.Enter enter(String showId, String userId, String admissionToken) {
		AdmissionTokenClaimsDTO claims = admissionTokenService.parseAdmissionToken(admissionToken, showId, userId);

		boolean deleteFlag = ticketingRedisRepository.deleteAdmissionToken(claims.getShowId(), claims.getUserId(), admissionToken);
		Preconditions.validate(deleteFlag, ErrorCode.INVALID_ADMISSION_TOKEN);

		String sessionToken = UUID.randomUUID().toString();

		// TODO: 만료시간 기획측과 논의
		ticketingRedisRepository.saveSessionToken(sessionToken, userId, showId, Duration.ofMinutes(5));
		ticketingRedisRepository.addActiveTicketingUser(showId, sessionToken);
		long sessionTokenTtl = ticketingRedisRepository.getSessionTokenTtl(sessionToken);

		return new TicketingResponse.Enter(sessionToken, sessionTokenTtl);
	}

}
