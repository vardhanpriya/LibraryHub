package com.libraryhub.inventory.service.impl;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.catalog.repository.BookRepository;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.repository.LibraryBranchRepository;
import com.libraryhub.inventory.dto.BookAcquisitionRequest;
import com.libraryhub.inventory.dto.BookAcquisitionResponse;
import com.libraryhub.inventory.entity.BookAcquisition;
import com.libraryhub.inventory.repository.BookAcquisitionRepository;
import com.libraryhub.inventory.service.BookAcquisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookAcquisitionServiceImpl implements BookAcquisitionService {

    private final BookAcquisitionRepository repository;
    private final BookRepository bookRepository;
    private final LibraryBranchRepository branchRepository;

    @Override
    public BookAcquisitionResponse create(BookAcquisitionRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        LibraryBranch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        BookAcquisition acquisition = new BookAcquisition();
        acquisition.setBook(book);
        acquisition.setBranch(branch);
        acquisition.setVendor(request.getVendor());
        acquisition.setCost(request.getCost());
        acquisition.setQuantity(request.getQuantity());
        acquisition.setPurchaseDate(request.getPurchaseDate());
        acquisition.setStatus(request.getStatus());
        acquisition.setReceivedBy(request.getReceivedBy());
        acquisition.setRemarks(request.getRemarks());

        acquisition = repository.save(acquisition);

        return mapToResponse(acquisition);
    }

    @Override
    public BookAcquisitionResponse update(Long id, BookAcquisitionRequest request) {

        BookAcquisition acquisition = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acquisition not found"));

        acquisition.setVendor(request.getVendor());
        acquisition.setCost(request.getCost());
        acquisition.setQuantity(request.getQuantity());
        acquisition.setPurchaseDate(request.getPurchaseDate());
        acquisition.setStatus(request.getStatus());
        acquisition.setReceivedBy(request.getReceivedBy());
        acquisition.setRemarks(request.getRemarks());

        return mapToResponse(repository.save(acquisition));
    }

    @Override
    @Transactional(readOnly = true)
    public BookAcquisitionResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Acquisition not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookAcquisitionResponse> search(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return repository.search(keyword, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id); // hard delete (inventory records usually not soft deleted)
    }

    private BookAcquisitionResponse mapToResponse(BookAcquisition a) {
        return BookAcquisitionResponse.builder()
                .acquisitionId(a.getAcquisitionId())
                .bookId(a.getBook().getBookId())
                .bookTitle(a.getBook().getTitle())
                .branchId(a.getBranch().getBranchId())
                .branchName(a.getBranch().getName())
                .vendor(a.getVendor())
                .cost(a.getCost())
                .quantity(a.getQuantity())
                .purchaseDate(a.getPurchaseDate())
                .status(a.getStatus())
                .receivedBy(a.getReceivedBy())
                .remarks(a.getRemarks())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
