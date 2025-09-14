package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InsurancePolicyService {

    private static final Logger logger = LoggerFactory.getLogger(InsurancePolicyService.class);

    @Autowired
    private InsurancePolicyRepository policyRepository;

    private final Set<Long> loggedPolicies = new HashSet<>();

    @Scheduled(cron = "0 * * * * *")
    public void logExpiredPolicies() {
//        if (LocalTime.now().isAfter(LocalTime.of(1, 0))) {
//            return;
//        }

        List<InsurancePolicy> expired = policyRepository.findByEndDate(LocalDate.now());
        for (InsurancePolicy policy : expired) {
            if (!loggedPolicies.contains(policy.getId())) {
                String information = String.format(
                        "Policy %d for car %d expired on %s",
                        policy.getId(),
                        policy.getCar().getId(),
                        policy.getEndDate()
                );
                logger.info(information);
                loggedPolicies.add(policy.getId());
            }
        }
    }
}
