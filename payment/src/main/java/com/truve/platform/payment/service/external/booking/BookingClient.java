package com.truve.platform.payment.service.external.booking;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.truve.platform.payment.service.dto.PaymentRequest;
import com.truve.platform.payment.service.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingClient {
	public static final String TOPIC = "booking.payment";
	public static final String GROUP = "booking-payment-group";

	private final ObjectMapper mapper;
	private final PaymentService paymentService;

	@KafkaListener(topics = TOPIC, groupId = GROUP)
	public void consume(String payload) {
		JsonNode node = mapper.readTree(payload);

		switch (node.get("type").asString()) {
			case "CREATE" -> create(node);
		}
	}

	private void create(JsonNode node) {
		paymentService.create(new PaymentRequest.Create(node.get("orderId").asString(), node.get("amount").asLong()));
	}
}
