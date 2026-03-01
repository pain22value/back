package com.truve.platform.payment.service.service.external.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TossRequest {

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Confirm {
		private String orderId;
		private Long amount;
		private String paymentKey;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Cancel {
		private String cancelReason;
		private Long cancelAmount;
		private RefundReceiveAccount refundReceiveAccount;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RefundReceiveAccount {
		private String bank;
		private String accountNumber;
		private String holderName;
	}

}
