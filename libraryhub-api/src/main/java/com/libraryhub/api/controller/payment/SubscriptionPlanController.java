package com.libraryhub.api.controller.payment;


import com.libraryhub.payment.dto.request.SubscriptionPlanRequestDTO;
import com.libraryhub.payment.dto.response.SubscriptionPlanResponseDTO;
import com.libraryhub.payment.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription-plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private final SubscriptionPlanService service;

    @PostMapping
    public SubscriptionPlanResponseDTO createPlan(
            @RequestBody SubscriptionPlanRequestDTO dto) {
        return service.createPlan(dto);
    }

    @GetMapping
    public List<SubscriptionPlanResponseDTO> getAllPlans() {
        return service.getAllPlans();
    }

    @GetMapping("/{id}")
    public SubscriptionPlanResponseDTO getPlan(@PathVariable Long id) {
        return service.getPlanById(id);
    }

    @PutMapping("/{id}")
    public SubscriptionPlanResponseDTO updatePlan(
            @PathVariable Long id,
            @RequestBody SubscriptionPlanRequestDTO dto) {

        return service.updatePlan(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable Long id) {
        service.deletePlan(id);
    }
}
