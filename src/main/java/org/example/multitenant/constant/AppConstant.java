package org.example.multitenant.constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AppConstant {

    public static final String LOGIN_URI = "/api/auth/login";
    public static final String BEARER = "Bearer ";
}
