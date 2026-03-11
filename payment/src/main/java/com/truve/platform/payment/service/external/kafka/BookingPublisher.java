package com.truve.platform.payment.service.external.kafka;

import org.springframework.stereotype.Component;

import com.truve.platform.common.event.EventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingPublisher {
	private static final String TOPIC = "payment.booking";
	private static final String CONFIRMED_EVENT_TYPE = "CONFIRMED";

	private final EventPublisher eventPublisher;

	public void publish(BookingEventCommand.Confirmed command) {
		eventPublisher.publish(TOPIC, command.getReservationNumber(), CONFIRMED_EVENT_TYPE, command);
	}
}
