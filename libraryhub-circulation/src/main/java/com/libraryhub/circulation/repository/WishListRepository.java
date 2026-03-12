package com.libraryhub.circulation.repository;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.circulation.entity.WishList;
import com.libraryhub.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList,Long> {
    Page<WishList> findByUser(User user, Pageable pageable);
    boolean existsByUserAndBook(User user, Book book);

    void deleteByUserAndBook(User user, Book book);
}
