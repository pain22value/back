package org.truve.platform.ticketing.service.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TicketingInternalResponse {

	@Getter
	@AllArgsConstructor
	public static class RemainingSeats {
		private Long ShowScheduleId;
		private Grades[] grades;
	}

	@Getter
	@AllArgsConstructor
	private static class Grades {
		private String gradeName;
		private Long remainingSeatCount;
		private Long totalCount;
	}
}
