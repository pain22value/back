package com.truve.platform.performance.service.domain.entity;

import java.time.LocalDateTime;

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
@Table(name = "performance_schedule")
public class PerformanceSchedule extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_id", nullable = false)
	private Performance performance;

	@Column(nullable = false)
	private LocalDateTime dateTime;

	@Column(nullable = false)
	private Boolean isAvailable;

	@Builder
	private PerformanceSchedule(Performance performance, LocalDateTime dateTime, Boolean isAvailable) {
		this.performance = performance;
		this.dateTime = dateTime;
		this.isAvailable = isAvailable;
	}
}
