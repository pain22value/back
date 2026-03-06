package org.truve.platform.ticketing.service.schedule.dto;

import org.truve.platform.ticketing.service.schedule.constant.SeatStatus;

import lombok.Getter;

@Getter
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
