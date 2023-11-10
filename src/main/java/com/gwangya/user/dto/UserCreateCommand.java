package com.gwangya.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserCreateCommand {

    private String email;

    private String password;

    protected UserCreateCommand() {
    }
}
