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
@Table(name = "musical_schedule_actors")
public class MusicalActor extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id", nullable = false)
	private MusicalSchedule schedule;

	@Column(nullable = false)
	private Long actorId;

	@Column(nullable = false)
	private String role;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Boolean isLiked;

	@Builder
	private MusicalActor(MusicalSchedule schedule, Long actorId, String role, String name, Boolean isLiked) {
		this.schedule = schedule;
		this.actorId = actorId;
		this.role = role;
		this.name = name;
		this.isLiked = isLiked;
	}
}
