package com.truve.platform.payment.service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truve.platform.common.response.ApiResult;
import com.truve.platform.payment.service.dto.PaymentRequest;
import com.truve.platform.payment.service.dto.PaymentResponse;
import com.truve.platform.payment.service.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
	private final PaymentService paymentService;

	@PostMapping
	public ApiResult<PaymentResponse.Create> create(@RequestBody PaymentRequest.Create request) {
		Long paymentId = paymentService.create(request);
		var response = new PaymentResponse.Create(paymentId);

		return ApiResult.ok(response);
	}
}
