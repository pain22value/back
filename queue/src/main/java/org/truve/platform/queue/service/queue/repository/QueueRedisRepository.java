package org.truve.platform.queue.service.queue.repository;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueueRedisRepository {

	private static final String WAIT_KEY_PREFIX = "q:wait:";
	private static final String READY_KEY_PREFIX = "q:ready:";
	private static final String SHOW_SET_KEY = "q:shows";

	private final StringRedisTemplate redisTemplate;

	public void registerShow(String showId) {
		redisTemplate.opsForSet().add(SHOW_SET_KEY, showId);
	}

	public void enqueue(String showId, String userId, long enqueueTime) {
		redisTemplate.opsForZSet().addIfAbsent(waitKey(showId), userId, enqueueTime);
	}

	public Optional<Long> getRank(String showId, String userId) {
		var rank = redisTemplate.opsForZSet().rank(waitKey(showId), userId);
		return Optional.ofNullable(rank);
	}



	private static String waitKey(String showId) {
		return WAIT_KEY_PREFIX + showId;
	}

	private static String readyKey(String showId, String userId) {
		return READY_KEY_PREFIX + showId + ":" + userId;
	}
}
