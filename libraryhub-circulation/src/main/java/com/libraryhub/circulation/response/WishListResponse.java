package com.libraryhub.circulation.response;


import com.libraryhub.catalog.entity.Book;
import com.libraryhub.identity.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListResponse {
    private Long wishListId;
    private Long  userId;
    private String userFullName;
    private Long bookId;
    private LocalDateTime addedAt;
    private String notes;
}
