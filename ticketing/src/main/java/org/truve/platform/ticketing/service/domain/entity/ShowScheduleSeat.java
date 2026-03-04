package org.truve.platform.ticketing.service.domain.entity;

import org.truve.platform.ticketing.service.constant.SeatStatus;

import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;
import com.truve.platform.common.support.BaseEntity;

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
@Table(name = "show_schedule_seat_mapping")
public class ShowScheduleSeat extends BaseEntity {

	@Column(nullable = false)
	private Long showScheduleId;

	@Column(nullable = false)
	private Long seatId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SeatStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "show_section_price_id")
	private ShowSectionPrice showSectionPrice;

	@Builder
	public ShowScheduleSeat(
		Long showScheduleId,
		Long seatId,
		ShowSectionPrice showSectionPrice
		) {

		this.showScheduleId = showScheduleId;
		this.seatId = seatId;
		this.showSectionPrice = showSectionPrice;
		this.status = SeatStatus.AVAILABLE;
	}

	public boolean  isAvailable() {
		return status == SeatStatus.AVAILABLE;
	}

	public void cancelSeat() {
		this.status = SeatStatus.AVAILABLE;
	}

	public void purchaseSeat() {
		if (this.status == SeatStatus.SOLD) {
			throw new CustomException(ErrorCode.ALREADY_SOLD_SEAT);
		}
		this.status = SeatStatus.SOLD;
	}

}
