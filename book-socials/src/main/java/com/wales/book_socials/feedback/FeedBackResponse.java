package com.wales.book_socials.feedback;

import lombok.*;

import java.util.UUID;

//@Setter
//@Getter
@Builder
public record FeedBackResponse(
        UUID uuid,
        Double note,
        String comment,
        UUID bookUuid,
        boolean ownFeedback) {
}
