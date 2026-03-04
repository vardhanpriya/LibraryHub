package com.libraryhub.api.controller.circulation;


import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.circulation.response.CreateFineResponse;
import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.repository.FineRepository;
import com.libraryhub.circulation.request.CreateFineRequest;
import com.libraryhub.circulation.service.FineService;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/fines")
@RequiredArgsConstructor
public class FineController
{


    private final FineService fineService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CreateFineResponse>>> getUserFines(@PathVariable Long userId) {
        List<CreateFineResponse> fineResponse = fineService.getUserFines(userId);
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(fineResponse));
    }

    @PostMapping("/create-fine")
    public ResponseEntity<ApiResponse<CreateFineResponse>> createFine(@RequestBody CreateFineRequest fineRequest){
        CreateFineResponse createFineResponse =  fineService.createFine(fineRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(createFineResponse));
    }
}
