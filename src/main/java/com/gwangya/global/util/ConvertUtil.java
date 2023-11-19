package com.gwangya.global.util;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public final class ConvertUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
		.registerModule(new JavaTimeModule());

	public static <T, R> T convert(R from, Class<T> to) {
		if (Objects.isNull(from)) {
			return null;
		}
		return objectMapper.convertValue(from, to);
	}

	public static <T> T convert(String jsonBody, Class<T> to) throws JsonProcessingException {
		if (ObjectUtils.isEmpty(jsonBody)) {
			return null;
		}
		return objectMapper.readValue(jsonBody, to);
	}
}

