package com.libraryhub.circulation.service.impl;

import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.entity.Loan;
import com.libraryhub.circulation.repository.FineRepository;
import com.libraryhub.circulation.repository.LoanRepository;
import com.libraryhub.circulation.request.CreateFineRequest;
import com.libraryhub.circulation.response.CreateFineResponse;
import com.libraryhub.circulation.service.FineService;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    @Override
    public List<CreateFineResponse> getUserFines(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch all fines (or use findUnpaidByUser if you want only unpaid)
        List<Fine> fines = fineRepository.findByUser(user);

        return fines.stream()
                .map(f -> CreateFineResponse.builder()
                        .loanId(f.getLoan().getLoanId())
                        .amount(f.getAmount())
                        .dueDate(f.getLoan().getDueDate())
                        .branchName(f.getLoan().getBranch().getName())
                        .bookTitle(f.getLoan().getBookCopy().getBook().getTitle())
                        .status(f.getStatus().name())
                        .paidAmount(f.getPaidAmount())
                        .paidDate(f.getPaidDate())
                        .build())
                .toList();

    }

    @Override
    public CreateFineResponse createFine(CreateFineRequest fineRequest) {
        Loan loanEntity = loanRepository.findById(fineRequest.getLoanId())
                .orElseThrow(()->new RuntimeException("Loan Not found with loanId : " + fineRequest.getLoanId()));
        User userEntity = userRepository.findById(fineRequest.getWaivedBy()).orElseThrow(()->new RuntimeException("User found with the id you provided for waiving"));
        Fine fine = new Fine();
            fine.setLoan(loanEntity);
            fine.setCreatedAt(LocalDateTime.now());
            fine.setAmount(fineRequest.getAmount());
            fine.setPaidAmount(fineRequest.getPaidAmount());
            fine.setStatus(fineRequest.getStatus());
            fine.setPaidDate(LocalDate.now());
            fine.setWaivedBy(userEntity);

        Fine savedFine = fineRepository.saveAndFlush(fine);

       return CreateFineResponse.builder()
                .loanId(savedFine.getLoan().getLoanId())
                .amount(savedFine.getAmount())
                .dueDate(savedFine.getLoan().getDueDate())
                .branchName(savedFine.getLoan().getBranch().getName())
                .bookTitle(savedFine.getLoan().getBookCopy().getBook().getTitle())
                .status(savedFine.getStatus().name())
                .paidAmount(savedFine.getPaidAmount())
                .paidDate(savedFine.getPaidDate())
                .build();
    }
}
