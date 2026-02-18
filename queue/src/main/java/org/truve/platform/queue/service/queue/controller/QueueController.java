package org.truve.platform.queue.service.queue.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.truve.platform.queue.service.common.response.ApiResult;
import org.truve.platform.queue.service.queue.service.QueueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {

	private static final String USER_ID_HEADER = "X-User-Id";

	private final QueueService queueService;

	@PostMapping("/{showId}/enter")
	public ApiResult<Void> enter(
		@PathVariable String showId,
		@RequestHeader(value = USER_ID_HEADER, required = false) String userId
	) {
		queueService.enter(showId, userId);
		return ApiResult.ok();
	}

}
