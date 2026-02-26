package com.truve.platform.performance.service.domain.entity;

import com.truve.platform.common.support.BaseEntity;
import com.truve.platform.performance.service.domain.constant.ActorRole;

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
@Table(name = "performance_casting")
public class PerformanceCasting extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_schedule_id", nullable = false)
	private PerformanceSchedule performanceSchedule;

	@Column(nullable = false)
	private Long actorId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ActorRole role;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Boolean isLiked;

	@Builder
	private PerformanceCasting(PerformanceSchedule performanceSchedule, Long actorId, ActorRole role, String name,
		Boolean isLiked) {
		this.performanceSchedule = performanceSchedule;
		this.actorId = actorId;
		this.role = role;
		this.name = name;
		this.isLiked = isLiked;
	}
}
