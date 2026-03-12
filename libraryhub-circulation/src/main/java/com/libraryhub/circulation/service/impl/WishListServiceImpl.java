package com.libraryhub.circulation.service.impl;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.catalog.repository.BookRepository;
import com.libraryhub.circulation.entity.WishList;
import com.libraryhub.circulation.repository.WishListRepository;
import com.libraryhub.circulation.response.WishListResponse;
import com.libraryhub.circulation.service.WishListService;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    @Override
    public WishListResponse addToWishList(Long userId,Long bookId, String notes) {
        User user = userRepository.findById(userId).orElseThrow();

        //validae book exist or not
        Book  book = bookRepository.findById(bookId).orElseThrow(()->new RuntimeException("Book Not found.."));

        // check if book is already in wishlist
        if(wishListRepository.existsByUserAndBook(user,book)){
                throw new RuntimeException("Book in wishlist already exists");
        }
        //create wishlist
        WishList wishList = new WishList();
        wishList.setBook(book);
        wishList.setUser(user);
        wishList.setNotes(notes);
        WishList savedWishList = wishListRepository.save(wishList);

        WishListResponse response = new WishListResponse();
            response.setWishListId(savedWishList.getWishListId());
            response.setNotes(wishList.getNotes());
            response.setAddedAt(savedWishList.getAddedAt());
            response.setUserFullName(user.getName());
            response.setUserId(user.getId());
            response.setBookId(bookId);
        return response;
    }

    @Override
    @Transactional
    public void removeFromWishList(Long userId,Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if(!wishListRepository.existsByUserAndBook(user,book)){
            throw new RuntimeException("Book is not in your wishlist");
        }
        wishListRepository.deleteByUserAndBook(user,book);
    }

    @Override
    public Page<WishListResponse> getMyWishList(int page, int size ,Long userId) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("addedAt").descending());
        User user = userRepository.findById(userId).orElseThrow();
        Page<WishList> wishLists = wishListRepository.findByUser(user,pageable);

        return wishLists.map(wish -> WishListResponse.builder()
                .wishListId(wish.getWishListId())
                .userId(wish.getUser().getId())
                .userFullName(wish.getUser().getName())
                .bookId(wish.getBook().getBookId())
                .addedAt(wish.getAddedAt())
                .notes(wish.getNotes())
                .build());
    }
}
