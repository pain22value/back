package com.truve.platform.performance.service.domain.entity;

import com.truve.platform.common.support.BaseEntity;
import com.truve.platform.performance.service.domain.constant.SeatGrade;

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
@Table(name = "performance_seat_grade")
public class PerformanceSeatGrade extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_id", nullable = false)
	private Performance performance;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SeatGrade seatGrade;

	@Column(nullable = false)
	private Integer price;

	@Builder
	private PerformanceSeatGrade(Performance performance, SeatGrade seatGrade, Integer price) {
		this.performance = performance;
		this.seatGrade = seatGrade;
		this.price = price;
	}
}
