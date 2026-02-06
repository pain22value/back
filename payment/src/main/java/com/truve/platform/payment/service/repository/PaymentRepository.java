package com.truve.platform.payment.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truve.platform.payment.service.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	boolean existsByOrderId(String orderId);
}
