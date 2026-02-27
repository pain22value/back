package com.truve.platform.performance.service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.truve.platform.performance.service.domain.constant.PerformanceScheduleStatus;
import com.truve.platform.performance.service.domain.entity.Artist;
import com.truve.platform.performance.service.domain.entity.Performance;
import com.truve.platform.performance.service.domain.entity.PerformanceCasting;
import com.truve.platform.performance.service.domain.entity.PerformanceSchedule;
import com.truve.platform.performance.service.domain.entity.PerformanceScheduleCasting;
import com.truve.platform.performance.service.domain.entity.PerformanceSeatGrade;
import com.truve.platform.performance.service.domain.entity.Venue;
import com.truve.platform.performance.service.dto.PerformanceResponse;
import com.truve.platform.performance.service.repository.PerformanceRepository;
import com.truve.platform.performance.service.repository.PerformanceScheduleCastingRepository;
import com.truve.platform.performance.service.repository.PerformanceScheduleRepository;
import com.truve.platform.performance.service.repository.PerformanceSeatGradeRepository;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

	@Mock
	private PerformanceRepository performanceRepository;
	@Mock
	private PerformanceScheduleRepository performanceScheduleRepository;
	@Mock
	private PerformanceScheduleCastingRepository performanceScheduleCastingRepository;
	@Mock
	private PerformanceSeatGradeRepository performanceSeatGradeRepository;

	@InjectMocks
	private PerformanceService performanceService;

	@Test
	@DisplayName("회차별 캐스팅은 회차 기준으로 그룹핑되고 order 오름차순으로 정렬된다.")
	void 공연_상세_회차별_캐스팅_그룹핑_정렬_성공() {
		Long performanceId = 1L;

		Performance performance = org.mockito.Mockito.mock(Performance.class);
		Venue venue = org.mockito.Mockito.mock(Venue.class);
		when(performance.getId()).thenReturn(performanceId);
		when(performance.getTitle()).thenReturn("뮤지컬");
		when(performance.getDescription()).thenReturn("설명");
		when(performance.getRuntimeMin()).thenReturn(120);
		when(performance.getAgeLimit()).thenReturn(8);
		when(performance.getPosterUrl()).thenReturn("https://img/poster.jpg");
		when(performance.getNoticeUrl()).thenReturn(null);
		when(performance.getStartTime()).thenReturn(LocalDateTime.of(2026, 3, 1, 0, 0));
		when(performance.getEndTime()).thenReturn(LocalDateTime.of(2026, 4, 1, 0, 0));
		when(performance.getVenue()).thenReturn(venue);
		when(venue.getId()).thenReturn(10L);
		when(venue.getName()).thenReturn("예술의전당");
		when(venue.getAddress()).thenReturn("서울");

		PerformanceSchedule schedule1 = org.mockito.Mockito.mock(PerformanceSchedule.class);
		PerformanceSchedule schedule2 = org.mockito.Mockito.mock(PerformanceSchedule.class);
		when(schedule1.getId()).thenReturn(2001L);
		when(schedule1.getPerformanceTime()).thenReturn(LocalDateTime.of(2026, 3, 2, 19, 30));
		when(schedule1.getStatus()).thenReturn(PerformanceScheduleStatus.OPEN);
		when(schedule2.getId()).thenReturn(2002L);
		when(schedule2.getPerformanceTime()).thenReturn(LocalDateTime.of(2026, 3, 3, 19, 30));
		when(schedule2.getStatus()).thenReturn(PerformanceScheduleStatus.CANCELLED);

		Artist artistA = org.mockito.Mockito.mock(Artist.class);
		Artist artistB = org.mockito.Mockito.mock(Artist.class);
		Artist artistC = org.mockito.Mockito.mock(Artist.class);
		when(artistA.getId()).thenReturn(101L);
		when(artistA.getName()).thenReturn("배우A");
		when(artistB.getId()).thenReturn(102L);
		when(artistB.getName()).thenReturn("배우B");
		when(artistC.getId()).thenReturn(103L);
		when(artistC.getName()).thenReturn("배우C");

		PerformanceCasting castOrder2 = org.mockito.Mockito.mock(PerformanceCasting.class);
		PerformanceCasting castOrder1 = org.mockito.Mockito.mock(PerformanceCasting.class);
		PerformanceCasting castOtherSchedule = org.mockito.Mockito.mock(PerformanceCasting.class);
		when(castOrder2.getId()).thenReturn(5002L);
		when(castOrder2.getArtist()).thenReturn(artistB);
		when(castOrder2.getRoleName()).thenReturn("조연");
		when(castOrder2.getCastingOrder()).thenReturn(2);
		when(castOrder1.getId()).thenReturn(5001L);
		when(castOrder1.getArtist()).thenReturn(artistA);
		when(castOrder1.getRoleName()).thenReturn("주연");
		when(castOrder1.getCastingOrder()).thenReturn(1);
		when(castOtherSchedule.getId()).thenReturn(5003L);
		when(castOtherSchedule.getArtist()).thenReturn(artistC);
		when(castOtherSchedule.getRoleName()).thenReturn("특별출연");
		when(castOtherSchedule.getCastingOrder()).thenReturn(1);

		PerformanceScheduleCasting sc1 = org.mockito.Mockito.mock(PerformanceScheduleCasting.class);
		PerformanceScheduleCasting sc2 = org.mockito.Mockito.mock(PerformanceScheduleCasting.class);
		PerformanceScheduleCasting sc3 = org.mockito.Mockito.mock(PerformanceScheduleCasting.class);
		when(sc1.getPerformanceSchedule()).thenReturn(schedule1);
		when(sc1.getPerformanceCasting()).thenReturn(castOrder2);
		when(sc2.getPerformanceSchedule()).thenReturn(schedule1);
		when(sc2.getPerformanceCasting()).thenReturn(castOrder1);
		when(sc3.getPerformanceSchedule()).thenReturn(schedule2);
		when(sc3.getPerformanceCasting()).thenReturn(castOtherSchedule);

		PerformanceSeatGrade seat = org.mockito.Mockito.mock(PerformanceSeatGrade.class);
		when(seat.getId()).thenReturn(7001L);
		when(seat.getGradeName()).thenReturn("VIP");
		when(seat.getBasePrice()).thenReturn(150000);
		when(seat.getColorCode()).thenReturn("#FFD700");

		when(performanceRepository.findByIdOrThrow(performanceId)).thenReturn(performance);
		when(performanceScheduleRepository.findSchedules(performanceId)).thenReturn(List.of(schedule1, schedule2));
		when(performanceScheduleCastingRepository.findAllByScheduleIds(List.of(2001L, 2002L)))
			.thenReturn(List.of(sc1, sc2, sc3));
		when(performanceSeatGradeRepository.findSeatPrices(performanceId)).thenReturn(List.of(seat));

		PerformanceResponse.Detail result = performanceService.getDetail(performanceId);

		assertEquals(2, result.getSchedules().size());
		assertEquals("OPEN", result.getSchedules().get(0).getStatus());
		assertEquals("CANCELLED", result.getSchedules().get(1).getStatus());

		List<PerformanceResponse.Casting> firstScheduleCastings = result.getSchedules().get(0).getCastings();
		assertEquals(2, firstScheduleCastings.size());
		assertEquals("배우A", firstScheduleCastings.get(0).getArtistName());
		assertEquals(1, firstScheduleCastings.get(0).getOrder());
		assertEquals("배우B", firstScheduleCastings.get(1).getArtistName());
		assertEquals(2, firstScheduleCastings.get(1).getOrder());

		List<PerformanceResponse.Casting> secondScheduleCastings = result.getSchedules().get(1).getCastings();
		assertEquals(1, secondScheduleCastings.size());
		assertEquals("배우C", secondScheduleCastings.get(0).getArtistName());

		assertFalse(firstScheduleCastings.get(0).getIsLiked());
		assertFalse(firstScheduleCastings.get(1).getIsLiked());
	}
}
