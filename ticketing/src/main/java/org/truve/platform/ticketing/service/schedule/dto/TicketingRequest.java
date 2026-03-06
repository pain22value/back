package org.truve.platform.ticketing.service.schedule.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TicketingRequest {

	@Getter
	@AllArgsConstructor
	public static class HoldSeat {
		@NotNull
		List<Long> seatIds;
	}
}
