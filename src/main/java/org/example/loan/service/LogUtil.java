package org.example.loan.service;

import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LogUtil {

    public static String getTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
