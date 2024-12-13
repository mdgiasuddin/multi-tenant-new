package org.example.multitenant.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.multitenant.config.datasource.TenantContext;
import org.example.multitenant.model.entity.Tenant;
import org.example.multitenant.repository.StudentRepository;
import org.example.multitenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final StudentRepository studentRepository;
    private final TenantRepository tenantRepository;

    @Value("${tenant.default}")
    private String defaultTenant;

    @Scheduled(cron = "${scheduler.time}", zone = "Asia/Dhaka")
    public void schedule() {
        TenantContext.setCurrentTenant(defaultTenant);
        List<Tenant> tenants = tenantRepository.findAll();
        log.info("Tenants: {}", tenants);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (Tenant tenant : tenants) {
            executorService.execute(new StudentScheduler(tenant.getName(), studentRepository));
        }
    }
}
