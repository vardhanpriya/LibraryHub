package com.libraryhub.inventory.service.impl;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.catalog.repository.BookRepository;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.repository.LibraryBranchRepository;
import com.libraryhub.inventory.dto.BookCopyRequest;
import com.libraryhub.inventory.dto.BookCopyResponse;
import com.libraryhub.inventory.entity.BookAcquisition;
import com.libraryhub.inventory.entity.BookCopy;
import com.libraryhub.inventory.repository.BookAcquisitionRepository;
import com.libraryhub.inventory.repository.BookCopyRepository;
import com.libraryhub.inventory.service.BookCopyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyRepository repository;
    private final BookRepository bookRepository;
    private final LibraryBranchRepository branchRepository;
    private final BookAcquisitionRepository acquisitionRepository;

    @Override
    public BookCopyResponse create(BookCopyRequest request) {

        if (repository.existsByBarcode(request.getBarcode())) {
            throw new RuntimeException("Barcode already exists");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        LibraryBranch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        BookAcquisition acquisition = null;
        if (request.getAcquisitionId() != null) {
            acquisition = acquisitionRepository.findById(request.getAcquisitionId())
                    .orElseThrow(() -> new RuntimeException("Acquisition not found"));
        }

        BookCopy copy = new BookCopy();
        copy.setBarcode(request.getBarcode());
        copy.setShelfLocation(request.getShelfLocation());
        copy.setStatus("AVAILABLE");
        copy.setCondition(request.getCondition());
        copy.setBook(book);
        copy.setBranch(branch);
        copy.setAcquisition(acquisition);
        copy.setCreatedAt(LocalDateTime.now());

        return mapToResponse(repository.save(copy));
    }

    @Override
    public BookCopyResponse update(Long id, BookCopyRequest request) {

        BookCopy copy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copy not found"));

        copy.setShelfLocation(request.getShelfLocation());
        copy.setStatus(request.getStatus());
        copy.setCondition(request.getCondition());
        copy.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(repository.save(copy));
    }

    @Override
    public void delete(Long id) {
        BookCopy copy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copy not found"));

        copy.setDeleted(true);
        repository.save(copy);
    }

    @Override
    public BookCopyResponse getById(Long id) {
        BookCopy copy = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Copy not found"));
        return mapToResponse(copy);
    }

    @Override
    public Page<BookCopyResponse> getAll(Pageable pageable) {
        return repository.findByIsDeletedFalse(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<BookCopyResponse> getByBook(Long bookId, Pageable pageable) {
        return repository.findByBookBookIdAndIsDeletedFalse(bookId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<BookCopyResponse> getByBranch(Long branchId, Pageable pageable) {
        return repository.findByBranchBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<BookCopyResponse> getByStatus(String status, Pageable pageable) {
        return repository.findByStatusAndIsDeletedFalse(status, pageable)
                .map(this::mapToResponse);
    }

    private BookCopyResponse mapToResponse(BookCopy copy) {
        return BookCopyResponse.builder()
                .copyId(copy.getCopyId())
                .barcode(copy.getBarcode())
                .shelfLocation(copy.getShelfLocation())
                .status(copy.getStatus())
                .condition(copy.getCondition())
                .bookId(copy.getBook().getBookId())
                .bookTitle(copy.getBook().getTitle())
                .branchId(copy.getBranch().getBranchId())
                .branchName(copy.getBranch().getName())
                .build();
    }
}
