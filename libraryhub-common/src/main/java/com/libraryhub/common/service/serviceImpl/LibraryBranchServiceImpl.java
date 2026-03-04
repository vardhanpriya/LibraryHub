package com.libraryhub.common.service.serviceImpl;

import com.libraryhub.common.constants.Constants;
import com.libraryhub.common.dto.LibraryBranchReq;
import com.libraryhub.common.dto.LibraryBranchResp;
import com.libraryhub.common.entity.City;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.repository.CityRepository;
import com.libraryhub.common.repository.LibraryBranchRepository;
import com.libraryhub.common.service.LibraryBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LibraryBranchServiceImpl implements LibraryBranchService {

    private final LibraryBranchRepository repository;
    private final CityRepository cityRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public LibraryBranchResp createBranch(LibraryBranchReq branch) {

        City cityEntity = cityRepository.findByCityCode(branch.getCityCode()).orElseThrow(() -> new RuntimeException("City not found"));

        Long seq = jdbcTemplate.queryForObject("SELECT nextval('branch_code_seq')", Long.class);
        String prefix = cityEntity.getCityCode().toUpperCase();
        String branchCode = prefix + "-" + String.format("%05d", seq);

        LibraryBranch builtEntity = LibraryBranch.builder()
                .name(branch.getName())
                .branchCode(branchCode)
                .phone(branch.getPhone())
                .email(branch.getEmail())
                .openingHours(branch.getOpeningHours())
                .maxCapacity(branch.getMaxCapacity())
                .status(Constants.ACTIVE)
                .city(cityEntity)
                .isDeleted(false)
                .address(branch.getAddress())
                .build();

        LibraryBranch saveBranch = repository.save(builtEntity);

        LibraryBranchResp build = LibraryBranchResp.builder()
                .branchId(saveBranch.getBranchId())
                .branchCode(saveBranch.getBranchCode())
                .name(saveBranch.getName())
                .address(saveBranch.getAddress())
                .phone(saveBranch.getPhone())
                .email(saveBranch.getEmail())
                .openingHours(saveBranch.getOpeningHours())
                .status(saveBranch.getStatus())
                .maxCapacity(saveBranch.getMaxCapacity())
                .cityName(saveBranch.getCity().getName())
                .cityCode(saveBranch.getCity().getCityCode())
                .build();

        return build;
    }

    @Override
    public List<LibraryBranchResp> getAllBranches() {
        return repository.findAll()
                .stream()
                .filter(branch -> !branch.isDeleted())
                .map(branch -> LibraryBranchResp.builder()
                        .branchId(branch.getBranchId())
                        .name(branch.getName())
                        .branchCode(branch.getBranchCode())
                        .address(branch.getAddress())
                        .phone(branch.getPhone())
                        .email(branch.getEmail())
                        .openingHours(branch.getOpeningHours())
                        .status(branch.getStatus())
                        .maxCapacity(branch.getMaxCapacity())
                        .cityName(branch.getCity().getName())
                        .cityCode(branch.getCity().getCityCode())
                        .stateName(branch.getCity().getState().getName())
                        .build())
                .toList();
    }

    @Override
    public LibraryBranchResp getBranchById(Long id) {
        Optional<LibraryBranch> data =   repository.findById(id).filter(branch -> !branch.isDeleted());
        if(data.isPresent()){
          return   LibraryBranchResp.builder()
                    .branchId(data.get().getBranchId())
                    .name(data.get().getName())
                    .branchCode(data.get().getBranchCode())
                    .address(data.get().getAddress())
                    .phone(data.get().getPhone())
                    .email(data.get().getEmail())
                    .openingHours(data.get().getOpeningHours())
                    .status(data.get().getStatus())
                    .maxCapacity(data.get().getMaxCapacity())
                    .cityCode(data.get().getCity().getCityCode())
                    .cityName(data.get().getCity().getName())
                    .build();

        }else {
            throw  new RuntimeException("Branch Not found");
        }
    }

    @Override
    public LibraryBranch updateBranch(Long id, LibraryBranch updatedBranch) {
        return repository.findById(id)
                .map(branch -> {
                    branch.setName(updatedBranch.getName());
                    branch.setAddress(updatedBranch.getAddress());
                    branch.setPhone(updatedBranch.getPhone());
                    branch.setEmail(updatedBranch.getEmail());
                    branch.setOpeningHours(updatedBranch.getOpeningHours());
                    branch.setStatus(updatedBranch.getStatus());
                    branch.setMaxCapacity(updatedBranch.getMaxCapacity());
//                    branch.setCity(updatedBranch.getCity());
                    return repository.save(branch);
                }).orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    @Override
    public void deleteBranch(Long id) {
        repository.findById(id).ifPresent(branch -> {
            branch.setDeleted(true);
            repository.save(branch);
        });
    }

    // Search by name
    @Override
    public List<LibraryBranch> searchByName(String name) {
        return repository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);
    }

    // Search by city
    @Override
    public List<LibraryBranch> searchByCity(Long cityId) {
        return repository.findByCityCityIdAndIsDeletedFalse(cityId);
    }

    // Search by name + city
    @Override
    public List<LibraryBranch> searchByNameAndCity(String name, Long cityId) {
        return repository.findByNameContainingIgnoreCaseAndCityCityIdAndIsDeletedFalse(name, cityId);
    }
}
