package com.truve.platform.musical.service.domain.entity;

import com.truve.platform.common.support.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "musical_seat_prices")
public class MusicalSeatPrice extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "musical_id", nullable = false)
	private Musical musical;

	@Column(nullable = false)
	private String seatGrade;

	@Column(nullable = false)
	private Integer price;

	@Builder
	private MusicalSeatPrice(Musical musical, String seatGrade, Integer price) {
		this.musical = musical;
		this.seatGrade = seatGrade;
		this.price = price;
	}
}
