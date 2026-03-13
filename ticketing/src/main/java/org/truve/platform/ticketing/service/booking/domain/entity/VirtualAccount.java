package org.truve.platform.ticketing.service.booking.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VirtualAccount {
	private String accountNumber;
	private String bank;
	private String customerName;
	private LocalDateTime dueDate;

	@Builder
	public VirtualAccount(String accountNumber, String bank, String customerName, LocalDateTime dueDate) {
		this.accountNumber = accountNumber;
		this.bank = bank;
		this.customerName = customerName;
		this.dueDate = dueDate;
	}
}
