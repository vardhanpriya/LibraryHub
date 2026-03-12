package com.libraryhub.api.controller.circulation;


import com.libraryhub.api.response.ApiResponse;
import com.libraryhub.api.response.ApiResponseBuilder;
import com.libraryhub.circulation.response.WishListResponse;
import com.libraryhub.circulation.service.WishListService;
import com.libraryhub.identity.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;


    @PostMapping("/add/{bookId}")
    public ResponseEntity<ApiResponse<WishListResponse>> addToWishList(@PathVariable Long bookId,
                                                     @RequestParam(required = false) String notes,
                                                     @AuthenticationPrincipal CustomUserDetails user  ){
        WishListResponse response = wishListService.addToWishList(user.getUserId(), bookId, notes);
       return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseBuilder.success(response));
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<ApiResponse<String>> removeFromWishList(@PathVariable Long bookId,
                                                                  @AuthenticationPrincipal CustomUserDetails user){
        wishListService.removeFromWishList(user.getUserId(),bookId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success("removed book from wishlist"));
    }

    @GetMapping("/my-wishlist")
    public ResponseEntity<?> getMyWishList(
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @AuthenticationPrincipal CustomUserDetails user){
        Page<WishListResponse> myWishList = wishListService.getMyWishList(page, size, user.getUserId());
      return   ResponseEntity.status(HttpStatus.OK).body(ApiResponseBuilder.success(myWishList));
    }

}
