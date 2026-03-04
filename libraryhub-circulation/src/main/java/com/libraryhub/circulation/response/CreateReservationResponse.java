package com.libraryhub.circulation.response;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.identity.entity.User;
import com.libraryhub.inventory.entity.BookCopy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReservationResponse {

    private Long reservationId;
    private LocalDate reservationDate;
    private LocalDate expiryDate;
    private Integer priority = 1;

    private ReservationStatus status; // could be ACTIVE, READY_FOR_PICKUP, CANCELLED, etc.

    private UserDto user;

//    private BookCopy copy;

    private BookDto book;

    private LibraryBranchDto branch;

    private LocalDateTime notifiedAt;

    private LocalDateTime createdAt;

   @Data
    public static class UserDto {
        private Long userId;
        private String fullName;
        private String email;
    }

    @Data
    public static class BookDto {
        private Long bookId;
        private String title;
        private String isbn;
    }

    @Data
    public static class LibraryBranchDto{
        private Long branchId;
        private String name;
        private String branchCode;
    }
}
