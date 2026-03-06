package org.truve.platform.ticketing.service.schedule.domain.entity;

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
@Table(name = "seat_section")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SeatSection extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venue_id")
	private Venue venue;

	@Column(nullable = false)
	private String section;

	@Column(nullable = false)
	private Long floor;


	@Builder
	public SeatSection(Venue venue, String section, Long floor) {
		this.venue = venue;
		this.section = section;
		this.floor = floor;
	}
}
