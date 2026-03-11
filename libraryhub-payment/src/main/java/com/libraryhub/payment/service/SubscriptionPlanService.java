package com.libraryhub.payment.service;

import com.libraryhub.payment.dto.request.SubscriptionPlanRequestDTO;
import com.libraryhub.payment.dto.response.SubscriptionPlanResponseDTO;
import com.libraryhub.payment.entity.SubscriptionPlan;

import java.util.List;


public interface SubscriptionPlanService {
     SubscriptionPlanResponseDTO createPlan(SubscriptionPlanRequestDTO dto);

     List<SubscriptionPlanResponseDTO> getAllPlans();

    SubscriptionPlanResponseDTO getPlanById(Long id) ;

  SubscriptionPlanResponseDTO updatePlan(Long id, SubscriptionPlanRequestDTO dto);



     void deletePlan(Long id) ;



}
