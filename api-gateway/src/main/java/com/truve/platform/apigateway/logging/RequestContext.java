package com.truve.platform.apigateway.logging;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.web.server.ServerWebExchange;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class RequestContext {
	long tsServer;
	String method;
	String path;
	String userId;
	String sessionTicket;
	Map<String, String> queryParams;
	String requestBody;
	Integer statusCode;

	public static RequestContext from(ServerWebExchange exchange, byte[] body) {
		return RequestContext.builder()
			.tsServer(System.currentTimeMillis())
			.method(exchange.getRequest().getMethod().name())
			.path(exchange.getRequest().getPath().value())
			.userId(exchange.getRequest().getHeaders().getFirst("X-User-Id"))
			.sessionTicket(exchange.getRequest().getHeaders().getFirst("X-Session-Ticket"))
			.queryParams(exchange.getRequest().getQueryParams().toSingleValueMap())
			.requestBody(parseBody(body))
			.build();
	}

	private static String parseBody(byte[] body) {
		if (body.length == 0)
			return "{}";
		return new String(body, StandardCharsets.UTF_8)
			.replaceAll("\\s+", " ")
			.trim();
	}
}