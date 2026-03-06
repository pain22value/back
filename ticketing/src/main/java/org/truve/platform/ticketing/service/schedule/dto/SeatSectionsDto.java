package org.truve.platform.ticketing.service.schedule.dto;

import org.truve.platform.ticketing.service.schedule.constant.SeatStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatSectionsDto {
	Long sectionId;
	String sectionName;
	String grade;
	Long price;
	Long seatId;
	String row;
	Long col;
	SeatStatus status;
}
