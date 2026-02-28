package com.libraryhub.circulation.service.impl;

import com.libraryhub.circulation.entity.Holiday;
import com.libraryhub.circulation.repository.HolidayRepository;
import com.libraryhub.circulation.request.HolidayRequest;
import com.libraryhub.circulation.response.HolidayResponse;
import com.libraryhub.circulation.service.HolidayService;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.repository.LibraryBranchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository repository;
    private final LibraryBranchRepository branchRepository;

    @Override
    public HolidayResponse create(HolidayRequest request) {

        // GLOBAL holiday validation
        if (request.getBranchId() == null) {

            if (repository.existsByDateAndBranchIsNull(request.getDate())) {
                throw new RuntimeException("Global holiday already exists for this date");
            }

        } else {

            if (repository.existsByDateAndBranchBranchId(
                    request.getDate(), request.getBranchId())) {
                throw new RuntimeException("Holiday already exists for this branch and date");
            }
        }

        LibraryBranch branch = null;

        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        Holiday holiday = Holiday.builder()
                .branch(branch)
                .date(request.getDate())
                .reason(request.getReason())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        return map(repository.save(holiday));
    }

    @Override
    public HolidayResponse update(Long id, HolidayRequest request) {

        Holiday holiday = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        LibraryBranch branch = null;

        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
        }

        holiday.setBranch(branch);
        holiday.setDate(request.getDate());
        holiday.setReason(request.getReason());
        holiday.setStatus(request.getStatus());

        return map(repository.save(holiday));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public HolidayResponse getById(Long id) {
        return map(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found")));
    }

    @Override
    public List<HolidayResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<HolidayResponse> getApplicableHolidays(Long branchId, LocalDate date) {

        return repository
                .findApplicableHoliday(
                        "ACTIVE",
                        date,
                        branchId
                )
                .stream()
                .map(this::map)
                .toList();
    }

    private HolidayResponse map(Holiday holiday) {
        return HolidayResponse.builder()
                .holidayId(holiday.getHolidayId())
                .branchId(holiday.getBranch() != null ? holiday.getBranch().getBranchId() : null)
                .branchName(holiday.getBranch() != null ? holiday.getBranch().getName() : "ALL_BRANCHES")
                .date(holiday.getDate())
                .reason(holiday.getReason())
                .status(holiday.getStatus())
                .build();
    }
}
