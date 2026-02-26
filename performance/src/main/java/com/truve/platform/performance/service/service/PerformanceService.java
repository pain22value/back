package com.truve.platform.performance.service.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truve.platform.performance.service.domain.entity.Performance;
import com.truve.platform.performance.service.domain.entity.PerformanceCasting;
import com.truve.platform.performance.service.domain.entity.PerformanceSchedule;
import com.truve.platform.performance.service.domain.entity.PerformanceSeatGrade;
import com.truve.platform.performance.service.dto.PerformanceResponse;
import com.truve.platform.performance.service.repository.PerformanceCastingRepository;
import com.truve.platform.performance.service.repository.PerformanceRepository;
import com.truve.platform.performance.service.repository.PerformanceScheduleRepository;
import com.truve.platform.performance.service.repository.PerformanceSeatGradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceService {

	private final PerformanceRepository performanceRepository;
	private final PerformanceScheduleRepository performanceScheduleRepository;
	private final PerformanceCastingRepository performanceCastingRepository;
	private final PerformanceSeatGradeRepository performanceSeatGradeRepository;

	@Transactional(readOnly = true)
	public PerformanceResponse.Detail getDetail(Long performanceId) {
		Performance performance = performanceRepository.findByIdOrThrow(performanceId);

		List<PerformanceSchedule> schedules = performanceScheduleRepository.findSchedules(performanceId);
		List<Long> scheduleIds = schedules.stream()
			.map(PerformanceSchedule::getId)
			.toList();

		Map<Long, List<PerformanceResponse.Actor>> actorsByScheduleId = scheduleIds.isEmpty()
			? Map.of()
			: performanceCastingRepository.findCastingsByScheduleIds(scheduleIds).stream()
				.sorted(Comparator.comparing(actor -> actor.getRole().getOrder()))
				.collect(Collectors.groupingBy(
					actor -> actor.getPerformanceSchedule().getId(),
					Collectors.mapping(this::toActorResponse, Collectors.toList())
				));

		List<PerformanceResponse.Schedule> scheduleResponses = schedules.stream()
			.map(schedule -> toScheduleResponse(schedule, actorsByScheduleId))
			.toList();

		List<PerformanceResponse.SeatPrice> seatPrices = performanceSeatGradeRepository
			.findSeatPrices(performanceId).stream()
			.sorted(Comparator.comparing(seatPrice -> seatPrice.getSeatGrade().getOrder()))
			.map(this::toSeatPriceResponse)
			.toList();

		return PerformanceResponse.Detail.builder()
			.performanceId(performance.getId())
			.title(performance.getTitle())
			.posterUrl(performance.getPosterUrl())
			.stage(performance.getStage())
			.runningTime(performance.getRunningTime())
			.ageLimit(performance.getAgeLimit())
			.priceInfo(performance.getPriceInfo())
			.startDate(performance.getStartDate())
			.endDate(performance.getEndDate())
			.openAt(performance.getOpenAt())
			.ratingAverage(performance.getRatingAverage())
			.weeklyRank(performance.getWeeklyRank())
			.reviewCount(performance.getReviewCount())
			.timeInfo(performance.getTimeInfo())
			.noticeUrl(performance.getNoticeUrl())
			.detailsUrl(performance.getDetailsUrl())
			.schedules(scheduleResponses)
			.seatPrices(seatPrices)
			.build();
	}

    // 회차별 배우 목록은 스케줄 기준으로 조합해 응답 순서를 고정한다.
	private PerformanceResponse.Schedule toScheduleResponse(
		PerformanceSchedule schedule,
		Map<Long, List<PerformanceResponse.Actor>> actorsByScheduleId
	) {
		List<PerformanceResponse.Actor> actors = actorsByScheduleId.getOrDefault(schedule.getId(), List.of());

		return PerformanceResponse.Schedule.builder()
			.scheduleId(schedule.getId())
			.dateTime(schedule.getDateTime())
			.isAvailable(schedule.getIsAvailable())
			.actors(actors)
			.build();
	}

    // 엔티티를 API 응답용 배우 DTO로 변환한다.
	private PerformanceResponse.Actor toActorResponse(PerformanceCasting actor) {
		return PerformanceResponse.Actor.builder()
			.actorId(actor.getActorId())
			.role(actor.getRole().getLabel())
			.name(actor.getName())
			.isLiked(actor.getIsLiked())
			.build();
	}

    // 좌석 등급/가격을 응답 DTO로 변환한다.
	private PerformanceResponse.SeatPrice toSeatPriceResponse(PerformanceSeatGrade seatPrice) {
		return PerformanceResponse.SeatPrice.builder()
			.seatGrade(seatPrice.getSeatGrade().getLabel())
			.price(seatPrice.getPrice())
			.build();
	}
}
