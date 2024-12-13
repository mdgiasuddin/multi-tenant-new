package org.example.multitenant.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.example.multitenant.config.datasource.TenantContext;
import org.example.multitenant.model.entity.Student;
import org.example.multitenant.repository.StudentRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Slf4j
public class StudentScheduler implements Runnable {
    private final String tenantName;
    private final StudentRepository studentRepository;

    public StudentScheduler(String tenantName, StudentRepository studentRepository) {
        this.tenantName = tenantName;
        this.studentRepository = studentRepository;
    }

    @Override
    public void run() {
        TenantContext.setCurrentTenant(tenantName);
        try {
            Random random = new SecureRandom();
            List<Student> students = studentRepository.findAll();
            for (Student student : students) {
                student.setName(tenantName + "_" + random.nextLong(50));
            }
            studentRepository.saveAll(students);
        } catch (Exception e) {
            log.error("Exception occurred during scheduled operation", e);
        }
    }
}
