package org.truve.platform.ticketing.service.booking.domain.policy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.truve.platform.ticketing.service.booking.domain.entity.Reservation;

import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.Preconditions;

public class CancellationPolicy {

	public static Long calculate(Reservation reservation, LocalDateTime cancelAt) {
		LocalDateTime showAt = reservation.getShowInfo().getStartAt();
		long daysUntilShow = ChronoUnit.DAYS.between(cancelAt.toLocalDate(), showAt.toLocalDate());
		long daysSinceBooked = ChronoUnit.DAYS.between(reservation.getBookedAt().toLocalDate(), cancelAt.toLocalDate());
		boolean isBookedDay = cancelAt.toLocalDate().equals(reservation.getBookedAt().toLocalDate());

		Preconditions.validate(daysUntilShow > 0, ErrorCode.CANCEL_NOT_ALLOWED);

		if (isFreeCancelPeriod(isBookedDay, daysUntilShow, daysSinceBooked))
			return 0L;

		if (daysUntilShow <= 9)
			return calculatePercentFee(reservation, daysUntilShow);

		return calculateFlatFee(reservation);
	}

	private static boolean isFreeCancelPeriod(boolean isBookedDay, long daysUntilShow, long daysSinceBooked) {
		return isBookedDay || (daysSinceBooked <= 7 && daysUntilShow > 9);
	}

	private static Long calculatePercentFee(Reservation reservation, long daysUntilShow) {
		if (daysUntilShow <= 1)
			return applyPercent(reservation, 30);
		if (daysUntilShow <= 3)
			return applyPercent(reservation, 20);
		return applyPercent(reservation, 10);
	}

	private static Long calculateFlatFee(Reservation reservation) {
		long flatFee = (long)reservation.getTickets().size() * 4000L;
		long maxFee = applyPercent(reservation, 10);
		return Math.min(flatFee, maxFee);
	}

	private static Long applyPercent(Reservation reservation, int percent) {
		return reservation.getTickets().stream()
			.mapToLong(t -> t.getPriceSnapshot() * percent / 100)
			.sum();
	}
}
