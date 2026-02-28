package com.libraryhub.circulation.service;

import com.libraryhub.circulation.request.HolidayRequest;
import com.libraryhub.circulation.response.HolidayResponse;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService {

    HolidayResponse create(HolidayRequest request);

    HolidayResponse update(Long id, HolidayRequest request);

    void delete(Long id);

    HolidayResponse getById(Long id);

    List<HolidayResponse> getAll();

    List<HolidayResponse> getApplicableHolidays(Long branchId, LocalDate date);
}
