package com.truve.platform.payment.service.external.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookingRequest {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Create {
		private String type;
		private String orderId;
		private Long amount;
	}
}
