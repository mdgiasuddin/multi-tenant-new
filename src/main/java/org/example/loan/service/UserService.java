package org.example.loan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.loan.model.entity.User;
import org.example.loan.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
//        log.info("Tenant service: " + TenantContext.getCurrentTenant());
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found!"));
    }
}
