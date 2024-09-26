package com.wales.book_socials.history;

import com.wales.book_socials.book.Book;
import com.wales.book_socials.common.BaseEntity;
import com.wales.book_socials.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {

    // many transactions to a user
    @ManyToOne
    @JoinColumn(name = "user_uuid", foreignKey = @ForeignKey(name = "user_transactions_fk"))
    private User user;

    // many transactions to a book
    @ManyToOne
    @JoinColumn(name = "book_uuid", foreignKey = @ForeignKey(name = "book_transactions_fk"))
    private Book book;

    private boolean returned;

    private boolean returnApproved;
}
