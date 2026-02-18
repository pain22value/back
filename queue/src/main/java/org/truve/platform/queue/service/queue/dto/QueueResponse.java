package org.truve.platform.queue.service.queue.dto;

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
		private final String status;
		private final Long rank;
		private final String admissionToken;
		private final Long expireTime;

		public static Status wait (Long rank) {
			return new Status("wait", rank, null, null);
		}
	}
}
