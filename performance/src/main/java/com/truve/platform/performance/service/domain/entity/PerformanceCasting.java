package com.truve.platform.performance.service.domain.entity;

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
@Table(name = "performance_casting")
public class PerformanceCasting extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_id", nullable = false)
	private Performance performance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "artist_id", nullable = false)
	private Artist artist;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "`order`")
	private Integer castingOrder;

	@Builder
	private PerformanceCasting(Performance performance, Artist artist, String roleName, Integer castingOrder) {
		this.performance = performance;
		this.artist = artist;
		this.roleName = roleName;
		this.castingOrder = castingOrder;
	}
}
