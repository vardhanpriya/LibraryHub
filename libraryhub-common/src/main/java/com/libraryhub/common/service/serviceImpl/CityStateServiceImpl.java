package com.libraryhub.common.service.serviceImpl;

import com.libraryhub.common.constants.Constants;
import com.libraryhub.common.dto.CommonResponse;
import com.libraryhub.common.dto.Status;
import com.libraryhub.common.entity.City;
import com.libraryhub.common.entity.State;
import com.libraryhub.common.repository.CityRepository;
import com.libraryhub.common.repository.StateRepository;
import com.libraryhub.common.request.CityRequest;
import com.libraryhub.common.request.StateRequest;
import com.libraryhub.common.response.CityResponse;
import com.libraryhub.common.response.StateResponse;
import com.libraryhub.common.service.CityStateService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityStateServiceImpl implements CityStateService {
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;


    @Override
    @Transactional(readOnly = true)
    public StateResponse getStateWithCities(Integer stateId) {

        try {
            State state = stateRepository.findByIdWithCities(stateId)
                    .orElseThrow(() -> new RuntimeException("State not found with stateId : " + stateId));

            return StateResponse.builder()
                    .stateId(state.getStateId())
                    .name(state.getName())
                    .code(state.getCode())
                    .status(state.getStatus())
                    .cities(
                            state.getCities()
                                    .stream()
                                    .map(city -> CityResponse.builder()
                                            .cityId(city.getCityId())
                                            .name(city.getName())
                                            .build())
                                    .collect(Collectors.toList())
                    )
                    .build();
        }catch (Exception ex){
            return  null;
        }
    }


    @Override
    @Transactional
    public StateResponse createState(StateRequest request) {
        State state;
        Optional<State> optionalState = stateRepository.findByNameAndCode(request.getName(), request.getCode());

        if(optionalState.isPresent()){
            state = optionalState.get();
            if (Constants.INACTIVE.equalsIgnoreCase(state.getStatus())) {
                state.setStatus(Constants.ACTIVE);
                state = stateRepository.save(state);
            }
        }else {
            state = State.builder().name(request.getName()).code(request.getCode()).status("ACTIVE").build();

            state = stateRepository.save(state);
        }
        return StateResponse.builder()
                .stateId(state.getStateId())
                .name(state.getName())
                .code(state.getCode())
                .status(state.getStatus())
                .cities(null)
                .build();
    }


    @Override
    public CityResponse createCity(CityRequest request) {
        State state = stateRepository.findById(request.getStateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        Optional<City> optionalCity = cityRepository.findByNameAndState(request.getName(), state);

        City city;
        if (optionalCity.isPresent()) {
            city = optionalCity.get();
            if (Constants.INACTIVE.equalsIgnoreCase(city.getStatus())) {
                city.setStatus(Constants.ACTIVE);
                city = cityRepository.save(city);
            }
        } else {
            city = City.builder()
                    .name(request.getName())
                    .status(Constants.ACTIVE)
                    .state(state)
                    .build();

            city = cityRepository.save(city);
        }
        return CityResponse.builder()
                .cityId(city.getCityId())
                .name(city.getName())
                .stateId(state.getStateId())
                .stateName(state.getName())
                .build();
    }

    @Override
    public CommonResponse deleteStateByStateId(Integer stateId) {
        try {
            State state = stateRepository.findById(stateId).orElseThrow();
            state.setStatus(Constants.INACTIVE);
            stateRepository.save(state);

            return CommonResponse.builder()
                    .status(Status.SUCCESS)
                    .responseCd("00")
                    .message("State Deleted With stateId " + stateId)
                    .build();
        }catch (Exception ex){
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return CommonResponse.builder()
                    .status(Status.ERROR)
                    .message("Data Not Deleted : " + "Exception found : " + ex.getMessage())
                    .responseCd("01")
                    .build();

        }


    }

    @Override
    public CommonResponse deleteCityByCityId(Integer cityId) {
        try {
            City city = cityRepository.findById(cityId).orElseThrow();
            city.setStatus(Constants.INACTIVE);
            cityRepository.save(city);

            return CommonResponse.builder()
                    .status(Status.SUCCESS)
                    .responseCd("00")
                    .message("State Deleted With cityId " + cityId)
                    .build();
        }catch (Exception ex){
            System.out.println(Arrays.toString(ex.getStackTrace()));
            return CommonResponse.builder()
                    .status(Status.ERROR)
                    .message("Data Not Deleted : " + "Exception found : " + ex.getMessage())
                    .responseCd("01")
                    .build();
        }
    }

    @Override
    public StateResponse getState(Integer id) {

      Optional<State> state =   stateRepository.findByStateIdAndStatus(id,Constants.ACTIVE);
      if(state.isPresent()){
          return  StateResponse.builder()
                  .stateId(state.get().getStateId())
                  .name(state.get().getName())
                  .code(state.get().getCode())
                  .status(state.get().getStatus())
                  .build();
      }else {
          throw new IllegalArgumentException("State Not found");
      }

    }
}
