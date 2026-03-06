package org.truve.platform.ticketing.service.domain.entity;

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
@Table(name = "show_section_price")
public class ShowSectionPrice extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "show_schedule_id")
	private ShowScheduled showScheduled;

	@Column(nullable = false)
	private Long seatSectionId;

	@Column(nullable = false)
	private Long showSectionGradeId;

	@Column(nullable = false)
	private Boolean isActive;

	@Column(nullable = false)
	private String grade;

	@Column(nullable = false)
	private Long price;

	@Builder
	public ShowSectionPrice(ShowScheduled showScheduled, Long seatSectionId, Long showSectionGradeId, Boolean isActive, String grade,  Long price) {
		this.showScheduled = showScheduled;
		this.seatSectionId = seatSectionId;
		this.showSectionGradeId = showSectionGradeId;
		this.isActive = isActive;
		this.grade = grade;
		this.price = price;
	}
}
