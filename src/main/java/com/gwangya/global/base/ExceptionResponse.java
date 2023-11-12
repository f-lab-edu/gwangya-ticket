package com.gwangya.global.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ExceptionResponse {

    private String message;
}
