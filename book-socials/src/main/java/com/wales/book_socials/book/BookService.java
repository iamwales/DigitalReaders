package com.wales.book_socials.book;

import com.wales.book_socials.common.PageResponse;
import com.wales.book_socials.exception.OperationNotPermittedException;
import com.wales.book_socials.history.BookTransactionHistory;
import com.wales.book_socials.history.BookTransactionHistoryRepository;
import com.wales.book_socials.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.wales.book_socials.book.BookSpecification.withOwnerUuid;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public BookResponse save(BookRequest bookRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        Book savedBook = bookRepository.save(book);

        return bookMapper.toBookResponse(savedBook);
    }

    public BookResponse findBookById(UUID uuid) {
        return bookRepository.findById(uuid)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book with the uuid %s not found".formatted(uuid)));

    }

    public PageResponse<BookResponse> findAllBooks(int page, int pageSize, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getUuid());
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();

        return new PageResponse<>(bookResponses, books.getNumber(),
                books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isFirst(),
                books.isLast());

    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int pageSize, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(withOwnerUuid(user.getUuid()), pageable);
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();

        return new PageResponse<>(bookResponses, books.getNumber(),
                books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isFirst(),
                books.isLast());
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int pageSize, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getUuid());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();

        return new PageResponse<>(bookResponses, allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(), allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(), allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast());
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int pageSize, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getUuid());
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();

        return new PageResponse<>(bookResponses, allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(), allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(), allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast());
    }

    public BookResponse updateShareableStatus(UUID bookUuid, Authentication connectedUser) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new EntityNotFoundException("Book with the uuid %s not found".formatted(bookUuid)));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getUuid(), user.getUuid())) {
            // throw exception
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }
        book.setSharable(!book.isSharable());
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookResponse(savedBook);
    }

    public BookResponse updateArchivedStatus(UUID bookUuid, Authentication connectedUser) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new EntityNotFoundException("Book with the uuid %s not found".formatted(bookUuid)));
        User user = ((User) connectedUser.getPrincipal());
        if(!Objects.equals(book.getOwner().getUuid(), user.getUuid())) {
            throw new OperationNotPermittedException("You cannot update others books archived status");
        }
        book.setArchived(!book.isArchived());
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookResponse(savedBook);
    }

    public BorrowedBookResponse borrowBook(UUID bookUuid, Authentication connectedUser) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the uuid %s".formatted(bookUuid)));

        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getUuid(), user.getUuid())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookUuid, user.getUuid());

        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory
                .builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();

        var newBorrowedBook = bookTransactionHistoryRepository.save(bookTransactionHistory);

        return bookMapper.toBorrowedBookResponse(newBorrowedBook);
    }

    public BorrowedBookResponse returnBorrowedBook(UUID bookUuid, Authentication connectedUser) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the uuid %s".formatted(bookUuid)));

        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());
        if(Objects.equals(book.getOwner().getUuid(), user.getUuid())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookUuidAndUserUuid(bookUuid, user.getUuid())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));
        bookTransactionHistory.setReturned(true);
        var savedBorrowedBook = bookTransactionHistoryRepository.save(bookTransactionHistory);

        return bookMapper.toBorrowedBookResponse(savedBorrowedBook);
    }


    public BorrowedBookResponse approveReturnedBorrowedBook(UUID bookUuid, Authentication connectedUser) {
        Book book = bookRepository.findById(bookUuid)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the uuid %s".formatted(bookUuid)));

        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = ((User) connectedUser.getPrincipal());

        // This check is not necessary as it can only be done by the book owner
        if(!Objects.equals(book.getOwner().getUuid(), user.getUuid())) {
            throw new OperationNotPermittedException("You are not the owner of this book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookUuidAndOwnerUuid(bookUuid, user.getUuid())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. So you cannot approve it's return"));
        bookTransactionHistory.setReturned(true);

        var savedBorrowedBook = bookTransactionHistoryRepository.save(bookTransactionHistory);

        return bookMapper.toBorrowedBookResponse(savedBorrowedBook);
    }
}
