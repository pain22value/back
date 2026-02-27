package com.truve.platform.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

	private HttpStatus status;
	private String message;

	public static ResponseEntity<ErrorData> error(HttpStatus status, String message, String errorCode) {
		return ResponseEntity.status(status).body(ErrorData.of(status.series().name(), message, errorCode));
	}

	@Getter
	@AllArgsConstructor
	public static class ErrorData {
		private String code;
		private String message;
		private String errorCode;
		public static ErrorData of(String code, String message, String errorCode) {
			return new ErrorData(code, message, errorCode);
		}
	}
}
