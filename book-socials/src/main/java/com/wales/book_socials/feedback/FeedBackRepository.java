package com.wales.book_socials.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface FeedBackRepository extends JpaRepository<FeedBack, UUID> {
    @Query("""
            SELECT feedBack
            FROM FeedBack feedBack
            WHERE feedBack.book.uuid = :bookUuid
            """)
    Page<FeedBack> findAllByBook(UUID bookUuid, Pageable pageable);
}
