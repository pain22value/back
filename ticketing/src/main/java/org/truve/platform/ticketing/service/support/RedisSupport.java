package org.truve.platform.ticketing.service.support;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truve.platform.common.exception.CustomException;
import com.truve.platform.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisSupport {

	private final StringRedisTemplate redisTemplate;
	private final ObjectMapper objectMapper;

	public void setValue(String key, String value, Duration duration) {
		redisTemplate.opsForValue().set(key, value, duration);
	}

	public String getValue(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public boolean delete(String key) {
		return redisTemplate.delete(key);
	}

	public void setJsonValue(String key, String value, Duration duration) {
		try {
			String json = objectMapper.writeValueAsString(value);
			setValue(key, json, duration);
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
		}
	}

	public <T> T getJsonValue(String key, Class<T> type) {
		String json = getValue(key);
		if (json == null) return null;
		try {
			return  objectMapper.readValue(json, type);
		} catch (JsonProcessingException e) {
			throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
		}
	}

	public boolean setIfAbsent(String key, String value, Duration duration) {
		Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, value, duration);
		return Boolean.TRUE.equals(flag);
	}

	public Boolean zAdd(String key, String member, double score) {
		return redisTemplate.opsForZSet().add(key, member, score);
	}

	public Long zRemRangeByScore(String key, double minScore, double maxScore) {
		return redisTemplate.opsForZSet().remove(key, minScore, maxScore);
	}
}
