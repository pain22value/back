package com.truve.platform.performance.service.domain.entity;

import com.truve.platform.common.support.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "performance_schedule_casting")
public class PerformanceScheduleCasting extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_cast_id", nullable = false)
	private PerformanceCasting performanceCasting;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_schedule_id", nullable = false)
	private PerformanceSchedule performanceSchedule;
}
