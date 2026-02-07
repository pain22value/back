package com.truve.platform.payment.service.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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

	@Value("${app.frontend.success-url}")
	private String successUrl;

	@Value("${app.frontend.fail-url}")
	private String failUrl;

	@PostMapping
	public ApiResult<PaymentResponse.Create> create(@RequestBody PaymentRequest.Create request) {
		Long paymentId = paymentService.create(request);
		var response = new PaymentResponse.Create(paymentId);

		return ApiResult.ok(response);
	}

	@GetMapping("/confirm")
	public ResponseEntity<Void> confirm(
		@RequestParam String paymentType,
		@RequestParam String orderId,
		@RequestParam String paymentKey,
		@RequestParam Long amount
	) {
		paymentService.confirm(orderId, paymentKey, amount);

		return ResponseEntity.status(HttpStatus.FOUND)
			.location(URI.create(successUrl + "?orderId=" + orderId))
			.build();
	}

	@GetMapping("/fail")
	public ResponseEntity<Void> fail(
		@RequestParam String code,
		@RequestParam String message,
		@RequestParam String orderId
	) {
		String redirectUrl = UriComponentsBuilder.fromPath(failUrl)
			.queryParam("code", code)
			.queryParam("message", message)
			.queryParam("orderId", orderId)
			.build()
			.toUriString();

		return ResponseEntity.status(HttpStatus.FOUND)
			.location(URI.create(redirectUrl))
			.build();
	}
}
