package com.wales.book_socials.book;

import com.wales.book_socials.file.FileUtils;
import com.wales.book_socials.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest bookRequest) {
        return Book.builder()
                .uuid(bookRequest.uuid())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .archived(false)
                .shareable(bookRequest.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .uuid(book.getUuid())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .owner(book.getOwner().getFullName())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                // implement this later
//                .bookCover(book.getBookCover())
                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory bookTransactionHistory) {
        return BorrowedBookResponse.builder()
                .uuid(bookTransactionHistory.getBook().getUuid())
                .title(bookTransactionHistory.getBook().getTitle())
                .authorName(bookTransactionHistory.getBook().getAuthorName())
                .isbn(bookTransactionHistory.getBook().getIsbn())
                .rate(bookTransactionHistory.getBook().getRate())
                .returned(bookTransactionHistory.isReturned())
                .returnApproved(bookTransactionHistory.isReturnApproved())
                .borrowedBy(bookTransactionHistory.getUser().getFullName())
                .borrowedByUserUuid(bookTransactionHistory.getUser().getUuid())
                .build();
    }
}
