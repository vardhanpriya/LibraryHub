package com.libraryhub.circulation.service;

import com.libraryhub.circulation.request.CreateFineRequest;
import com.libraryhub.circulation.response.CreateFineResponse;

import java.util.List;

public interface FineService {

    List<CreateFineResponse> getUserFines( Long userId);

    CreateFineResponse createFine(CreateFineRequest fineRequest);

}
