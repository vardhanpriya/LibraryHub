package com.libraryhub.circulation.service.impl;

import com.libraryhub.catalog.entity.Book;
import com.libraryhub.catalog.repository.BookRepository;
import com.libraryhub.circulation.entity.Reservation;
import com.libraryhub.circulation.enums.ReservationStatus;
import com.libraryhub.circulation.repository.ReservationRepository;
import com.libraryhub.circulation.response.CreateReservationResponse;
import com.libraryhub.circulation.service.ReservationService;
import com.libraryhub.common.entity.LibraryBranch;
import com.libraryhub.common.repository.LibraryBranchRepository;
import com.libraryhub.identity.entity.User;
import com.libraryhub.identity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LibraryBranchRepository branchRepository;
    @Override
    @Transactional
    public CreateReservationResponse createReservation(Long userId, Long bookId, Long branchId) {
        /*
        User user = userRepository.getReferenceById(userId);
Book book = bookRepository.getReferenceById(bookId);
LibraryBranch branch = branchRepository.getReferenceById(branchId);
         */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        LibraryBranch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        boolean exists = reservationRepository
                .existsByUserAndBookAndStatusIn(
                        user,
                        book,
                        List.of("ACTIVE", "READY_FOR_PICKUP")
                );

        if (exists) {
            throw new RuntimeException("Active reservation already exists");
        }

        Integer maxPriority = reservationRepository
                .getMaxPriority(book, branch,List.of(ReservationStatus.ACTIVE, ReservationStatus.READY_FOR_PICKUP));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setBranch(branch);
        reservation.setReservationDate(LocalDate.now());
        reservation.setExpiryDate(LocalDate.now().plusDays(7));
        reservation.setPriority(maxPriority + 1);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setCreatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.save(reservation);
        CreateReservationResponse.UserDto userDto = new CreateReservationResponse.UserDto();
                userDto.setUserId(saved.getUser().getId());
                userDto.setFullName(saved.getUser().getName());
                userDto.setEmail(saved.getUser().getEmail());
        CreateReservationResponse.BookDto bookDto = new CreateReservationResponse.BookDto();
                 bookDto.setBookId(saved.getBook().getBookId());
                 bookDto.setTitle(saved.getBook().getTitle());

        CreateReservationResponse.LibraryBranchDto branchDto = new CreateReservationResponse.LibraryBranchDto();
                     branchDto.setBranchId(saved.getBranch().getBranchId());
                      branchDto.setName(saved.getBranch().getName());
                      branchDto.setBranchCode(saved.getBranch().getBranchCode());

        CreateReservationResponse response = CreateReservationResponse.builder()
                .reservationId(saved.getReservationId())
                .user(userDto)
                .book(bookDto)
                .branch(branchDto)
                .reservationDate(saved.getReservationDate())
                .expiryDate(saved.getExpiryDate())
                .priority(saved.getPriority())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();

        return response;
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (reservation.getStatus() != ReservationStatus.ACTIVE &&
                reservation.getStatus() != ReservationStatus.READY_FOR_PICKUP) {
            throw new RuntimeException("Cannot cancel reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation saved = reservationRepository.save(reservation);
    }

    @Override
    public List<CreateReservationResponse> getAllReservationOfUser(Long userId) {
        List<CreateReservationResponse> responses = new ArrayList<>();
        User findyById = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found with id"));
        List<Reservation> reservationList = reservationRepository.findByUser(findyById);
        if(!reservationList.isEmpty()){
            reservationList.forEach(reservation -> {
                CreateReservationResponse.UserDto userDto = new CreateReservationResponse.UserDto();
                userDto.setUserId(reservation.getUser().getId());
                userDto.setFullName(reservation.getUser().getName());
                userDto.setEmail(reservation.getUser().getEmail());
                CreateReservationResponse.BookDto bookDto = new CreateReservationResponse.BookDto();
                bookDto.setBookId(reservation.getBook().getBookId());
                bookDto.setTitle(reservation.getBook().getTitle());

                CreateReservationResponse.LibraryBranchDto branchDto = new CreateReservationResponse.LibraryBranchDto();
                branchDto.setBranchId(reservation.getBranch().getBranchId());
                branchDto.setName(reservation.getBranch().getName());
                branchDto.setBranchCode(reservation.getBranch().getBranchCode());

                CreateReservationResponse response = CreateReservationResponse.builder()
                        .reservationId(reservation.getReservationId())
                        .user(userDto)
                        .book(bookDto)
                        .branch(branchDto)
                        .reservationDate(reservation.getReservationDate())
                        .expiryDate(reservation.getExpiryDate())
                        .priority(reservation.getPriority())
                        .status(reservation.getStatus())
                        .createdAt(reservation.getCreatedAt())
                        .build();

                responses.add(response);
            });
        }else {
            throw new RuntimeException("No reservation found current user");
        }
        return responses;
    }

    @Override
    public CreateReservationResponse getReservationById(Long id) {

        Reservation reservation = reservationRepository.findById(id).orElseThrow(()-> new RuntimeException("No Reservation found with id :" + id));
        CreateReservationResponse.UserDto userDto = new CreateReservationResponse.UserDto();
        userDto.setUserId(reservation.getUser().getId());
        userDto.setFullName(reservation.getUser().getName());
        userDto.setEmail(reservation.getUser().getEmail());
        CreateReservationResponse.BookDto bookDto = new CreateReservationResponse.BookDto();
        bookDto.setBookId(reservation.getBook().getBookId());
        bookDto.setTitle(reservation.getBook().getTitle());

        CreateReservationResponse.LibraryBranchDto branchDto = new CreateReservationResponse.LibraryBranchDto();
        branchDto.setBranchId(reservation.getBranch().getBranchId());
        branchDto.setName(reservation.getBranch().getName());
        branchDto.setBranchCode(reservation.getBranch().getBranchCode());

        CreateReservationResponse response = CreateReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .user(userDto)
                .book(bookDto)
                .branch(branchDto)
                .reservationDate(reservation.getReservationDate())
                .expiryDate(reservation.getExpiryDate())
                .priority(reservation.getPriority())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .build();

        return response;

    }

    @Override
    public List<CreateReservationResponse> searchReservations(Long userId, Long bookId, Long branchId,
                                                              ReservationStatus status, Integer minPriority,
                                                              Integer maxPriority, LocalDate fromDate, LocalDate toDate) {
        List<CreateReservationResponse> responses = new ArrayList<>();
        List<Reservation> reservationList = reservationRepository.searchReservations(userId, bookId, branchId, status, minPriority, maxPriority, fromDate, toDate);
        if(!reservationList.isEmpty()){
            reservationList.forEach(reservation -> {
                CreateReservationResponse.UserDto userDto = new CreateReservationResponse.UserDto();
                userDto.setUserId(reservation.getUser().getId());
                userDto.setFullName(reservation.getUser().getName());
                userDto.setEmail(reservation.getUser().getEmail());
                CreateReservationResponse.BookDto bookDto = new CreateReservationResponse.BookDto();
                bookDto.setBookId(reservation.getBook().getBookId());
                bookDto.setTitle(reservation.getBook().getTitle());

                CreateReservationResponse.LibraryBranchDto branchDto = new CreateReservationResponse.LibraryBranchDto();
                branchDto.setBranchId(reservation.getBranch().getBranchId());
                branchDto.setName(reservation.getBranch().getName());
                branchDto.setBranchCode(reservation.getBranch().getBranchCode());

                CreateReservationResponse response = CreateReservationResponse.builder()
                        .reservationId(reservation.getReservationId())
                        .user(userDto)
                        .book(bookDto)
                        .branch(branchDto)
                        .reservationDate(reservation.getReservationDate())
                        .expiryDate(reservation.getExpiryDate())
                        .priority(reservation.getPriority())
                        .status(reservation.getStatus())
                        .createdAt(reservation.getCreatedAt())
                        .build();

                responses.add(response);
            });
        }else {
            throw new RuntimeException("No reservation found for the filter you applied");
        }
        return responses;
    }
}
