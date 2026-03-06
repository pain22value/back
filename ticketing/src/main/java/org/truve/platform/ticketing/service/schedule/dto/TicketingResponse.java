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
		String title;
		String venueName;
		LocalDateTime startAt;
		public static TicketingResponse.Show from (String title,  String venueName, LocalDateTime startAt) {
			return new TicketingResponse.Show(title, title, startAt);
		}
	}
}
