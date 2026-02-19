package org.truve.platform.queue.service.queue.dto;

import org.truve.platform.queue.service.common.constants.QueueStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class QueueResponse {
	@Getter
	@RequiredArgsConstructor
	public static class Enter {

	}

	@Getter
	@AllArgsConstructor
	public static class Status {
		private final QueueStatus status;
		private final Long rank;
		private final String admissionToken;
		private final Long expireTime;

		public static Status wait (Long rank) {
			return new Status(QueueStatus.WAITING, rank, null, null);
		}

		public static Status ready (String admissionToken, long expiresTime) {
			return new Status(QueueStatus.READY, 0L, admissionToken, expiresTime);
		}
	}
}
