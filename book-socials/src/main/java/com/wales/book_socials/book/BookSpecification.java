package com.wales.book_socials.book;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class BookSpecification {
    public static Specification<Book> withOwnerUuid(UUID ownerUuid) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("uuid"), ownerUuid);

    }
}
