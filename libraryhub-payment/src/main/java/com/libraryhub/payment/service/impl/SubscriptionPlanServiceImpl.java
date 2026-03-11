package com.libraryhub.payment.service.impl;

import com.libraryhub.payment.dto.request.SubscriptionPlanRequestDTO;
import com.libraryhub.payment.dto.response.SubscriptionPlanResponseDTO;
import com.libraryhub.payment.entity.SubscriptionPlan;
import com.libraryhub.payment.repository.SubscriptionPlanRepository;
import com.libraryhub.payment.service.SubscriptionPlanService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository repository;


    @Override
    public SubscriptionPlanResponseDTO createPlan(SubscriptionPlanRequestDTO dto) {

        SubscriptionPlan plan = SubscriptionPlan.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .durationDays(dto.getDurationDays())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        SubscriptionPlan saved = repository.save(plan);

        return mapToResponse(saved);
    }


    @Override
    public List<SubscriptionPlanResponseDTO> getAllPlans() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public SubscriptionPlanResponseDTO getPlanById(Long id) {

        SubscriptionPlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        return mapToResponse(plan);
    }


    @Override
    public SubscriptionPlanResponseDTO updatePlan(Long id, SubscriptionPlanRequestDTO dto) {

        SubscriptionPlan plan = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setDurationDays(dto.getDurationDays());
        plan.setPrice(dto.getPrice());
        plan.setStatus(dto.getStatus());
        plan.setUpdatedAt(LocalDateTime.now());

        SubscriptionPlan updated = repository.save(plan);

        return mapToResponse(updated);
    }

    @Override
    public void deletePlan(Long id) {
        repository.deleteById(id);
    }

    private SubscriptionPlanResponseDTO mapToResponse(SubscriptionPlan plan) {
        return SubscriptionPlanResponseDTO.builder()
                .planId(plan.getPlanId())
                .name(plan.getName())
                .description(plan.getDescription())
                .durationDays(plan.getDurationDays())
                .price(plan.getPrice())
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .build();
    }
}
