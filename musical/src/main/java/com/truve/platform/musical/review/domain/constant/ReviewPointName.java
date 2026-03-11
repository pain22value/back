package com.truve.platform.musical.review.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReviewPointName {

	STAGE_PRODUCTION(ReviewPointCategory.CHARM, "무대연출"),
	STORY(ReviewPointCategory.CHARM, "스토리"),
	ACTING(ReviewPointCategory.CHARM, "배우연기"),
	DANCE(ReviewPointCategory.CHARM, "안무"),
	NUMBER(ReviewPointCategory.CHARM, "넘버"),

	IMMERSION(ReviewPointCategory.EMOTION, "몰입감"),
	TENSION(ReviewPointCategory.EMOTION, "텐션"),
	ENJOYMENT(ReviewPointCategory.EMOTION, "즐거움"),
	CATHARSIS(ReviewPointCategory.EMOTION, "카타르시스"),
	TOUCHING(ReviewPointCategory.EMOTION, "감동"),
	;

	private final ReviewPointCategory category;
	private final String label;
}
