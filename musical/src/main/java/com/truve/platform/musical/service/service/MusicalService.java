package com.truve.platform.musical.service.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truve.platform.musical.service.domain.entity.Musical;
import com.truve.platform.musical.service.domain.entity.MusicalActor;
import com.truve.platform.musical.service.domain.entity.MusicalSchedule;
import com.truve.platform.musical.service.domain.entity.MusicalSeatPrice;
import com.truve.platform.musical.service.dto.MusicalResponse;
import com.truve.platform.musical.service.repository.MusicalActorRepository;
import com.truve.platform.musical.service.repository.MusicalRepository;
import com.truve.platform.musical.service.repository.MusicalScheduleRepository;
import com.truve.platform.musical.service.repository.MusicalSeatPriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicalService {

	private final MusicalRepository musicalRepository;
	private final MusicalScheduleRepository musicalScheduleRepository;
	private final MusicalActorRepository musicalActorRepository;
	private final MusicalSeatPriceRepository musicalSeatPriceRepository;

	@Transactional(readOnly = true)
	public MusicalResponse.Detail getDetail(Long musicalId) {
		Musical musical = musicalRepository.findByIdOrThrow(musicalId);

		List<MusicalSchedule> schedules = musicalScheduleRepository.findSchedules(musicalId);
		List<Long> scheduleIds = schedules.stream()
			.map(MusicalSchedule::getId)
			.toList();

		Map<Long, List<MusicalResponse.Actor>> actorsByScheduleId = scheduleIds.isEmpty()
			? Map.of()
			: musicalActorRepository.findActorsByScheduleIds(scheduleIds).stream()
				.sorted(Comparator.comparing(actor -> actor.getRole().getOrder()))
				.collect(Collectors.groupingBy(
					actor -> actor.getSchedule().getId(),
					Collectors.mapping(this::toActorResponse, Collectors.toList())
				));

		List<MusicalResponse.Schedule> scheduleResponses = schedules.stream()
			.map(schedule -> toScheduleResponse(schedule, actorsByScheduleId))
			.toList();

		List<MusicalResponse.SeatPrice> seatPrices = musicalSeatPriceRepository.findSeatPrices(musicalId).stream()
			.sorted(Comparator.comparing(seatPrice -> seatPrice.getSeatGrade().getOrder()))
			.map(this::toSeatPriceResponse)
			.toList();

		return MusicalResponse.Detail.builder()
			.musicalId(musical.getId())
			.title(musical.getTitle())
			.posterUrl(musical.getPosterUrl())
			.stage(musical.getStage())
			.runningTime(musical.getRunningTime())
			.ageLimit(musical.getAgeLimit())
			.priceInfo(musical.getPriceInfo())
			.startDate(musical.getStartDate())
			.endDate(musical.getEndDate())
			.openAt(musical.getOpenAt())
			.ratingAverage(musical.getRatingAverage())
			.weeklyRank(musical.getWeeklyRank())
			.reviewCount(musical.getReviewCount())
			.timeInfo(musical.getTimeInfo())
			.noticeUrl(musical.getNoticeUrl())
			.detailsUrl(musical.getDetailsUrl())
			.schedules(scheduleResponses)
			.seatPrices(seatPrices)
			.build();
	}

    // 회차별 배우 목록은 스케줄 기준으로 조합해 응답 순서를 고정한다.
	private MusicalResponse.Schedule toScheduleResponse(
		MusicalSchedule schedule,
		Map<Long, List<MusicalResponse.Actor>> actorsByScheduleId
	) {
		List<MusicalResponse.Actor> actors = actorsByScheduleId.getOrDefault(schedule.getId(), List.of());

		return MusicalResponse.Schedule.builder()
			.scheduleId(schedule.getId())
			.dateTime(schedule.getDateTime())
			.isAvailable(schedule.getIsAvailable())
			.actors(actors)
			.build();
	}

    // 엔티티를 API 응답용 배우 DTO로 변환한다.
	private MusicalResponse.Actor toActorResponse(MusicalActor actor) {
		return MusicalResponse.Actor.builder()
			.actorId(actor.getActorId())
			.role(actor.getRole().getLabel())
			.name(actor.getName())
			.isLiked(actor.getIsLiked())
			.build();
	}

    // 좌석 등급/가격을 응답 DTO로 변환한다.
	private MusicalResponse.SeatPrice toSeatPriceResponse(MusicalSeatPrice seatPrice) {
		return MusicalResponse.SeatPrice.builder()
			.seatGrade(seatPrice.getSeatGrade().getLabel())
			.price(seatPrice.getPrice())
			.build();
	}
}
