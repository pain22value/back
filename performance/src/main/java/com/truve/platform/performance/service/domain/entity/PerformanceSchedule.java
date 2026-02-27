package com.truve.platform.performance.service.domain.entity;

import java.time.LocalDateTime;

import com.truve.platform.common.support.BaseEntity;
import com.truve.platform.performance.service.domain.constant.PerformanceScheduleStatus;

import jakarta.persistence.AttributeOverride;
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
@Table(name = "performance_schedule")
@AttributeOverride(name = "id", column = @Column(name = "performance_schedule_id"))
public class PerformanceSchedule extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_id", nullable = false)
	private Performance performance;

	@Column(nullable = false)
	private LocalDateTime performanceTime;

	@Column(nullable = false, length = 30)
	@Enumerated(EnumType.STRING)
	private PerformanceScheduleStatus status;

	private LocalDateTime date;

	@Builder
	private PerformanceSchedule(
		Performance performance,
		LocalDateTime performanceTime,
		PerformanceScheduleStatus status,
		LocalDateTime date
	) {
		this.performance = performance;
		this.performanceTime = performanceTime;
		this.status = status;
		this.date = date;
	}
}
