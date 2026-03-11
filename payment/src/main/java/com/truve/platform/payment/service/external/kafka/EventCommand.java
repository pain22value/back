package com.truve.platform.payment.service.external.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventCommand {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Create {
		private String orderId;
		private Long amount;
	}
}
