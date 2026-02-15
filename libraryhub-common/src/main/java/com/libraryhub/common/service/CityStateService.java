package com.libraryhub.common.service;


import com.libraryhub.common.dto.CommonResponse;
import com.libraryhub.common.request.CityRequest;
import com.libraryhub.common.request.StateRequest;
import com.libraryhub.common.response.CityResponse;
import com.libraryhub.common.response.StateResponse;

public interface CityStateService {
    StateResponse getStateWithCities(Integer stateId);


    StateResponse createState(StateRequest request);

    CityResponse createCity(CityRequest request);

    CommonResponse deleteStateByStateId(Integer stateId);

    CommonResponse deleteCityByCityId(Integer cityId);

    StateResponse getState(Integer id);
}
