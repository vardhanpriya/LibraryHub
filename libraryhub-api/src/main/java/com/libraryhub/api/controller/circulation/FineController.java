package com.libraryhub.api.controller.circulation;


import com.libraryhub.api.dto.FineResponse;
import com.libraryhub.circulation.entity.Fine;
import com.libraryhub.circulation.repository.FineRepository;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController
{

    private final FineRepository fineRepository;
    private final UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public List<FineResponse> getUserFines(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Fine> fines = fineRepository.findByLoanUserAndPaidFalse(user);

        return fines.stream()
                .map(f -> FineResponse.builder()
                        .loanId(f.getLoan().getLoanId())
                        .amount(f.getAmount())
                        .dueDate(f.getLoan().getDueDate())
                        .branchName(f.getLoan().getBranch().getName())
                        .bookTitle(f.getLoan().getCopy().getBook().getTitle())
                        .build())
                .toList();
    }
}
