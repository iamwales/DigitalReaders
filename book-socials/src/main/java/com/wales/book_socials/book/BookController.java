package com.wales.book_socials.book;

import com.wales.book_socials.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> saveBook(
            @Valid @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.save(bookRequest, connectedUser));
    }

    @GetMapping("{bookUuid}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable UUID bookUuid) {
        return ResponseEntity.ok(bookService.findBookById(bookUuid));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(page, pageSize, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, pageSize, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page, pageSize, connectedUser));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page, pageSize, connectedUser));
    }

    @PatchMapping("/shareable/{bookUuid}")
    public ResponseEntity<BookResponse> updateShareableStatus(
            @PathVariable("bookUuid") UUID bookUuid,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.updateShareableStatus(bookUuid, connectedUser));

    }

}
