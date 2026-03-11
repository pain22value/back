package org.truve.platform.ticketing.service.booking.client.payment;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.truve.platform.ticketing.service.booking.client.payment.dto.PaymentRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentClient {
	public static final String TOPIC = "booking.payment";

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ObjectMapper mapper;

	public void publish(PaymentRequest.Create request) {
		String payload = mapper.writeValueAsString(request);
		kafkaTemplate.send(TOPIC, request.getOrderId(), payload);
	}
}
