package com.truve.platform.payment.service.external.kafka;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookingEventCommand {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Confirmed {
		private String reservationNumber;
		@JsonFormat(shape = JsonFormat.Shape.ARRAY)
		private LocalDateTime paidAt;
		@JsonProperty("depositPending")
		private boolean isDepositPending;
	}
}
