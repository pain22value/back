package com.truve.platform.payment.service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.Preconditions;
import com.truve.platform.payment.service.domain.entity.Payment;
import com.truve.platform.payment.service.dto.PaymentRequest;
import com.truve.platform.payment.service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	@Transactional
	public Long create(PaymentRequest.Create request) {
		Preconditions.validate(!paymentRepository.existsByOrderId(request.getOrderId()), ErrorCode.ALREADY_EXIST_PAYMENT);

		Payment payment = Payment.builder()
			.orderId(request.getOrderId())
			.amount(request.getAmount())
			.method(request.getMethod())
			.build();

		return paymentRepository.save(payment).getId();
	}

	@Transactional
	public void confirm(String orderId, String paymentKey, Long amount) {
		Payment payment = paymentRepository.findByOrderIdOrThrow(orderId);

		payment.validateAmount(amount);

		// TODO Toss 결제 승인 API 호출

		payment.processConfirm(paymentKey);
	}
}
