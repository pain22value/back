package org.truve.platform.ticketing.service.domain.entity;

import java.time.LocalDateTime;

import org.truve.platform.ticketing.service.domain.constant.ShowScheduleStatus;

import com.truve.platform.common.support.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "show_schedule")
public class ShowSchedule extends BaseEntity {

	@Column
	private Long showId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ShowScheduleStatus status;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	LocalDateTime startTime;

	@Builder
	public ShowSchedule(Long showId, ShowScheduleStatus status, String title, String description, LocalDateTime startTime) {
		this.showId = showId;
		this.status = status;
		this.title = title;
		this.description = description;
		this.startTime = startTime;
	}
}
