package com.libraryhub.api.controller.common;


import com.libraryhub.common.dto.CommonResponse;
import com.libraryhub.common.request.CityRequest;
import com.libraryhub.common.request.StateRequest;
import com.libraryhub.common.response.CityResponse;
import com.libraryhub.common.response.StateResponse;
import com.libraryhub.common.service.CityStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/geolocator")
@RequiredArgsConstructor
public class CityStateController {

    private final CityStateService cityStateService;

    @GetMapping("/state/{id}")
    public StateResponse getStateWithCities(@PathVariable Integer id) {
        return cityStateService.getStateWithCities(id);
    }

    @DeleteMapping("/state/{stateId}")
    public CommonResponse deleteStateByStateId(@PathVariable Integer stateId) {
        return cityStateService.deleteStateByStateId(stateId);
    }

    @DeleteMapping("/city/{cityId}")
    public CommonResponse deleteCityByCityId(@PathVariable Integer cityId) {
        return cityStateService.deleteCityByCityId(cityId);
    }


    @PostMapping("/add-state")
    public ResponseEntity<StateResponse> createState(@RequestBody StateRequest request) {

        StateResponse response = cityStateService.createState(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-city")
    public ResponseEntity<CityResponse> addCity(@RequestBody CityRequest request) {

        CityResponse response = cityStateService.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}