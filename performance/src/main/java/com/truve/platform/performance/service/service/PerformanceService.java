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
import com.truve.platform.performance.service.domain.entity.PerformanceScheduleCasting;
import com.truve.platform.performance.service.domain.entity.PerformanceSeatGrade;
import com.truve.platform.performance.service.dto.PerformanceResponse;
import com.truve.platform.performance.service.repository.PerformanceRepository;
import com.truve.platform.performance.service.repository.PerformanceScheduleRepository;
import com.truve.platform.performance.service.repository.PerformanceScheduleCastingRepository;
import com.truve.platform.performance.service.repository.PerformanceSeatGradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceService {

	private final PerformanceRepository performanceRepository;
	private final PerformanceScheduleRepository performanceScheduleRepository;
	private final PerformanceScheduleCastingRepository performanceScheduleCastingRepository;
	private final PerformanceSeatGradeRepository performanceSeatGradeRepository;

	@Transactional(readOnly = true)
	public PerformanceResponse.Detail getDetail(Long performanceId) {
		Performance performance = performanceRepository.findByIdOrThrow(performanceId);

		List<PerformanceSchedule> schedules = performanceScheduleRepository.findSchedules(performanceId);
		List<Long> scheduleIds = schedules.stream()
			.map(PerformanceSchedule::getId)
			.toList();

		// 회차별 캐스팅 매핑을 한 번에 조회해 쿼리 수를 최소화한다.
		List<PerformanceScheduleCasting> scheduleCastings = scheduleIds.isEmpty()
			? List.of()
			: performanceScheduleCastingRepository.findAllByScheduleIds(scheduleIds);

		// 회차 ID를 key로 캐스팅을 묶어 schedules[].castings[] 형태로 조립한다.
		Map<Long, List<PerformanceResponse.Casting>> castingsByScheduleId = scheduleIds.isEmpty()
			? Map.of()
			: scheduleCastings.stream()
				.sorted(Comparator.comparing(
					sc -> sc.getPerformanceCasting().getCastingOrder(),
					Comparator.nullsLast(Comparator.naturalOrder())
				))
				.collect(Collectors.groupingBy(
					sc -> sc.getPerformanceSchedule().getId(),
					Collectors.mapping(sc -> toCastingResponse(sc.getPerformanceCasting()),
						Collectors.toList())
				));

		List<PerformanceResponse.Schedule> scheduleResponses = schedules.stream()
			.map(schedule -> toScheduleResponse(schedule, castingsByScheduleId))
			.toList();

		List<PerformanceResponse.SeatGrade> seatGrades = performanceSeatGradeRepository
			.findSeatPrices(performanceId).stream()
			.map(this::toSeatGradeResponse)
			.toList();

		return PerformanceResponse.Detail.builder()
			.performanceId(performance.getId())
			.title(performance.getTitle())
			.description(performance.getDescription())
			.runtimeMin(performance.getRuntimeMin())
			.ageLimit(performance.getAgeLimit())
			.posterUrl(performance.getPosterUrl())
			.noticeUrl(performance.getNoticeUrl())
			.startTime(performance.getStartTime())
			.endTime(performance.getEndTime())
			.venue(toVenueResponse(performance))
			.schedules(scheduleResponses)
			.seatGrades(seatGrades)
			.build();
	}

	private PerformanceResponse.Venue toVenueResponse(Performance performance) {
		return PerformanceResponse.Venue.builder()
			.venueId(performance.getVenue().getId())
			.name(performance.getVenue().getName())
			.address(performance.getVenue().getAddress())
			.build();
	}

	private PerformanceResponse.Schedule toScheduleResponse(
		PerformanceSchedule schedule,
		Map<Long, List<PerformanceResponse.Casting>> castingsByScheduleId
	) {
		List<PerformanceResponse.Casting> castings = castingsByScheduleId.getOrDefault(schedule.getId(), List.of());

		return PerformanceResponse.Schedule.builder()
			.scheduleId(schedule.getId())
			.performanceTime(schedule.getPerformanceTime())
			.status(schedule.getStatus().name())
			.castings(castings)
			.build();
	}

	private PerformanceResponse.Casting toCastingResponse(PerformanceCasting casting) {
		return PerformanceResponse.Casting.builder()
			.performanceCastId(casting.getId())
			.artistId(casting.getArtist().getId())
			.artistName(casting.getArtist().getName())
			.roleName(casting.getRoleName())
			.order(casting.getCastingOrder())
			// TODO: artist_likes 연동 후 로그인 사용자 기준 값으로 교체
			.isLiked(false)
			.build();
	}

	private PerformanceResponse.SeatGrade toSeatGradeResponse(PerformanceSeatGrade seatGrade) {
		return PerformanceResponse.SeatGrade.builder()
			.performanceSeatGradeId(seatGrade.getId())
			.gradeName(seatGrade.getGradeName())
			.basePrice(seatGrade.getBasePrice())
			.colorCode(seatGrade.getColorCode())
			.build();
	}
}
