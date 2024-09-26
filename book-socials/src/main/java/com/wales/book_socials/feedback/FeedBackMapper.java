package com.wales.book_socials.feedback;

import com.wales.book_socials.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class FeedBackMapper {
    public FeedBack toFeedBack(FeedBackRequest feedBackRequest) {
        return FeedBack
                .builder()
                .note(feedBackRequest.note())
                .comment(feedBackRequest.comment())
                .book(Book.builder().uuid(feedBackRequest.bookUuid()).build())
                .build();
    }

    public FeedBackResponse toFeedBackResponse(FeedBack feedBack, UUID userUuid) {
        return FeedBackResponse
                .builder()
                .uuid(feedBack.getUuid())
                .bookUuid(feedBack.getBook().getUuid())
                .comment(feedBack.getComment())
                .note(feedBack.getNote())
                .ownFeedback(Objects.equals(feedBack.getCreatedBy(), userUuid))
                .build();
    }
}
