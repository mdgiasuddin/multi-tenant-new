package org.example.loan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {
    private final RestTemplate restTemplate;

    public String getLoan() {
        log.info("LoanService: Get loan");
        String traceId = LogUtil.getTraceId();
        System.out.println("New trace id: " + traceId);
        MDC.put("traceId", traceId);
        log.info("LoanService: Get loan new");
        String fraudDetectResponse = restTemplate.getForObject("/api/fraud-detects", String.class);
        log.info("Fraud detect: {}", fraudDetectResponse);
        return String.format("Loan list: %s", UUID.randomUUID());
    }
}
