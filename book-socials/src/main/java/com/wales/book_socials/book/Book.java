package com.wales.book_socials.book;

import com.wales.book_socials.common.BaseEntity;
import com.wales.book_socials.feedback.FeedBack;
import com.wales.book_socials.history.BookTransactionHistory;
import com.wales.book_socials.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {
    private String title;

    private String authorName;

    private String isbn;

    private String synopsis;

    private String bookCover;

    private boolean archived;

    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_uuid")
    private User owner;

    // One book to many feedbacks
    @OneToMany(mappedBy = "book")
    private List<FeedBack> feedBacks;

    // One book to many histories
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if(feedBacks == null || feedBacks.isEmpty()) {
            return 0.0;
        }

        var rate = this.feedBacks.stream().mapToDouble(FeedBack::getNote).average().orElse(0.0);

        return Math.round(rate * 10.0) / 10.0;
    }

}
