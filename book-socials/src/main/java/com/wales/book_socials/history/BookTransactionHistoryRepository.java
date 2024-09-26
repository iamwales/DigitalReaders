package com.wales.book_socials.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, UUID> {
    @Query("""
            SELECT history
            FROM BookTransactionHistory
            WHERE history.user.uuid = :userUuid
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, UUID userUuid);

    @Query("""
            SELECT history
            FROM BookTransactionHistory
            WHERE history.book.owner.uuid = :userUuid
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, UUID userUuid);
}
