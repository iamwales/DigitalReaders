package com.wales.book_socials.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;
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

    @Query("""
            SELECT
            (COUNT(*) > 0) AS isBorrowed
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.uuid = :userUuid
            AND bookTransactionHistory.book.uuid = :bookUuid
            AND bookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(UUID bookUuid, UUID userUuid);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.user.uuid = :userUuid
            AND transaction.book.uuid = :bookUuid
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookUuidAndUserUuid(UUID bookUuid, UUID userUuid);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.book.owner.uuid = :userUuid
            AND transaction.book.uuid = :bookUuid
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findByBookUuidAndOwnerUuid(UUID bookUuid, UUID ownerUuid);
}
