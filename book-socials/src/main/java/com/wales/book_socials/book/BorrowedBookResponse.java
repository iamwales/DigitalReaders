package com.wales.book_socials.book;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookResponse {
    private UUID uuid;
    private String title;
    private String authorName;
    private String isbn;
    private String borrowedBy;
    private UUID borrowedByUserUuid;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
