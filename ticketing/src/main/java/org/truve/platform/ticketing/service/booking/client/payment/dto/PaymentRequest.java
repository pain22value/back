package org.truve.platform.ticketing.service.booking.client.payment.dto;

import org.truve.platform.ticketing.service.booking.domain.entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PaymentRequest {

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Create {
		private static final String CREATE_TYPE = "CREATE";

		private String type;
		private String orderId;
		private Long amount;

		public static Create of(Reservation reservation) {
			return Create.builder()
				.type(CREATE_TYPE)
				.orderId(reservation.getNumber())
				.amount(reservation.getTotalAmount())
				.build();
		}
	}
}
