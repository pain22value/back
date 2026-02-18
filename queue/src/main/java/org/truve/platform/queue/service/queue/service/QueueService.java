package org.truve.platform.queue.service.queue.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.truve.platform.queue.service.common.exception.CustomException;
import org.truve.platform.queue.service.common.exception.ErrorCode;
import org.truve.platform.queue.service.common.support.Preconditions;
import org.truve.platform.queue.service.queue.config.QueueProperties;
import org.truve.platform.queue.service.queue.dto.EnterQueueResponse;
import org.truve.platform.queue.service.queue.dto.LeaveQueueResponse;
import org.truve.platform.queue.service.queue.dto.QueueResponse;
import org.truve.platform.queue.service.queue.dto.QueueStatusResponse;
import org.truve.platform.queue.service.queue.repository.QueueRedisRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

	private final QueueRedisRepository queueRedisRepository;
	private final QueueProperties queueProperties;

	public void enter(String showId, String userId) {

		Preconditions.validate(StringUtils.hasText(showId), ErrorCode.INVALID_REQUEST_SHOW_ID);
		Preconditions.validate(StringUtils.hasText(userId), ErrorCode.INVALID_REQUEST_USER_ID);

		long now = System.currentTimeMillis();
		queueRedisRepository.registerShow(showId);
		queueRedisRepository.enqueue(showId, userId, now);
	}

	public QueueResponse.Status status(String showId, String userId) {

		Preconditions.validate(StringUtils.hasText(showId), ErrorCode.INVALID_REQUEST_SHOW_ID);
		Preconditions.validate(StringUtils.hasText(userId), ErrorCode.INVALID_REQUEST_USER_ID);

		var rank = queueRedisRepository.getRank(showId, userId);
		if (rank.isPresent()) {
			return QueueResponse.Status.wait(rank.get());
		}


	}

}
