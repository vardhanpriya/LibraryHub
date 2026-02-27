package com.libraryhub.api.controller.common;


import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.common.dto.CommonResponse;
import com.libraryhub.common.request.CityRequest;
import com.libraryhub.common.request.StateRequest;
import com.libraryhub.common.response.CityResponse;
import com.libraryhub.common.response.StateResponse;
import com.libraryhub.common.service.CityStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/geolocator")
@RequiredArgsConstructor
public class CityStateController {

    private final CityStateService cityStateService;

    @GetMapping("/state-with-cities/state-id/{id}")
    public ResponseEntity<ApiResponse<StateResponse>> getStateWithCities(@PathVariable Integer id) {
        StateResponse response = cityStateService.getStateWithCities(id);
        return  ResponseEntity.ok(ApiResponseBuilder.success(response));
    }

    @GetMapping("/state/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StateResponse>> getState(@PathVariable Integer id) {
        StateResponse response = cityStateService.getState(id);

        return ResponseEntity.ok(ApiResponseBuilder.success(response));
    }

    @DeleteMapping("/state/{stateId}")
    public ResponseEntity<ApiResponse<CommonResponse>> deleteStateByStateId(
            @PathVariable Integer stateId) {

        CommonResponse response = cityStateService.deleteStateByStateId(stateId);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(response)
        );
    }

    @DeleteMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<CommonResponse>> deleteCityByCityId(
            @PathVariable Integer cityId) {

        CommonResponse response = cityStateService.deleteCityByCityId(cityId);

        return ResponseEntity.ok(
                ApiResponseBuilder.success(response)
        );
    }


    @PostMapping("/add-state")
    public ResponseEntity<ApiResponse<StateResponse>> createState(
            @RequestBody StateRequest request) {

        StateResponse response = cityStateService.createState(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success(response));
    }

    @PostMapping("/add-city")
    public ResponseEntity<ApiResponse<CityResponse>> addCity(
            @RequestBody CityRequest request) {

        CityResponse response = cityStateService.createCity(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseBuilder.success(response));
    }
}