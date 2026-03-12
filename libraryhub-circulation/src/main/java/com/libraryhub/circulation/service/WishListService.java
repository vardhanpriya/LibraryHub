package com.libraryhub.circulation.service;

import com.libraryhub.circulation.response.WishListResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishListService {

    WishListResponse addToWishList(Long userId,Long bookId, String notes);
    void removeFromWishList(Long userId,Long bookId);
    Page<WishListResponse> getMyWishList(int page, int size , Long userId);
}
