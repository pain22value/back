package com.truve.platform.common.response;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Paging {
	int page;
	int size;

	public Pageable toPageable() {
		return PageRequest.of(page, size);
	}
}
