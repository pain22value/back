package org.truve.platform.ticketing.service.schedule.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TicketingResponse {

	@Getter
	@AllArgsConstructor
	public static class Enter {
		String sessionToken;
		long expireIn;
	}

	@Getter
	@AllArgsConstructor
	public static class ShowSeats {

	}

	@Getter
	@AllArgsConstructor
	public static class Show {
		Long showId;
		String title;
		LocalDateTime startTime;

		public static TicketingResponse.Show from (Long showId, String title, LocalDateTime startTime) {
			return new TicketingResponse.Show(showId, title, startTime);
		}
	}
}
