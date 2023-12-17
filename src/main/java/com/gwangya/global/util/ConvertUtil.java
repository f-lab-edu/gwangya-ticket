package com.gwangya.global.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.JsonParser;
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
		.configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
		.registerModule(new JavaTimeModule());

	public static <T, R> T convert(R from, Class<T> to) {
		if (Objects.isNull(from)) {
			return null;
		}
		return objectMapper.convertValue(from, to);
	}

	public static <T, R> List<T> convertList(List<R> from, Class<T> to) {
		List<T> result = new ArrayList<>();
		if (from.isEmpty()) {
			return result;
		}
		for (R r : from) {
			result.add(objectMapper.convertValue(r, to));
		}
		return result;
	}

	public static <T> T convert(String jsonBody, Class<T> to) throws JsonProcessingException {
		if (ObjectUtils.isEmpty(jsonBody)) {
			return null;
		}
		return objectMapper.readValue(jsonBody, to);
	}

	public static <T> String convert(T from) throws JsonProcessingException {
		if (Objects.isNull(from)) {
			return null;
		}
		return objectMapper.writeValueAsString(from);
	}
}

