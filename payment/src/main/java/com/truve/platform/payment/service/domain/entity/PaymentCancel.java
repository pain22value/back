package com.truve.platform.payment.service.domain.entity;

import java.time.LocalDateTime;

import com.truve.platform.common.support.BaseEntity;
import com.truve.platform.payment.service.domain.constant.CancelType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_cancels")
public class PaymentCancel extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@Column(nullable = false)
	private Long requestAmount;

	@Column(nullable = false)
	private Long refundFee;

	@Column(nullable = false)
	private Long refundAmount;

	@Column(nullable = false)
	private String cancelReason;

	@Column(nullable = false)
	private LocalDateTime canceledAt;

	@Column(nullable = false)
	private String transactionKey;

	@Column(nullable = false)
	private String cancelStatus;

	@Enumerated(EnumType.STRING)
	private CancelType type;

	@Builder
	public PaymentCancel(
		Payment payment,
		Long requestAmount,
		Long refundFee,
		String cancelReason,
		LocalDateTime canceledAt,
		String transactionKey,
		String cancelStatus,
		CancelType type) {
		this.payment = payment;
		this.requestAmount = requestAmount;
		this.refundFee = refundFee;
		this.refundAmount = requestAmount - refundFee;
		this.cancelReason = cancelReason;
		this.canceledAt = canceledAt;
		this.transactionKey = transactionKey;
		this.cancelStatus = cancelStatus;
		this.type = type;
	}

}
