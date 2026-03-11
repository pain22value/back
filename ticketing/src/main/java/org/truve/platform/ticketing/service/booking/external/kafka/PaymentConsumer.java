package org.truve.platform.ticketing.service.booking.external.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.truve.platform.ticketing.service.booking.service.BookingService;

import com.truve.platform.common.support.JsonConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {
	public static final String TOPIC = "payment.booking";
	public static final String GROUP = "payment-booking-group";

	private final JsonConverter jsonConverter;
	private final BookingService bookingService;

	@KafkaListener(topics = TOPIC, groupId = GROUP)
	public void consume(String payload, @Header("event-type") String type) {
		switch (type) {
			case "CONFIRMED" -> handleConfirmed(payload);
		}
	}

	private void handleConfirmed(String payload) {
		BookingEventCommand.Confirmed request = jsonConverter.convert(payload, BookingEventCommand.Confirmed.class);
		bookingService.confirm(request);
	}
}
