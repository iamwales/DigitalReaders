package com.wales.book_socials.book;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
public record BookResponse(
        UUID uuid,

        String title,

         String authorName,

         String isbn,

         String synopsis,

         String bookCover,

         String owner,

         double rate,

         boolean archived,

         boolean sharable,

         byte[] cover
) {
}
