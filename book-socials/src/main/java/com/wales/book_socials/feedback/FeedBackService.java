package com.wales.book_socials.feedback;

import com.wales.book_socials.book.Book;
import com.wales.book_socials.book.BookRepository;
import com.wales.book_socials.common.PageResponse;
import com.wales.book_socials.exception.OperationNotPermittedException;
import com.wales.book_socials.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final BookRepository bookRepository;
    private final FeedBackMapper feedBackMapper;
    private final FeedBackRepository feedBackRepository;

    public FeedBackResponse saveFeedBack(FeedBackRequest feedBackRequest, Authentication connectedUser) {
        Book book = bookRepository.findById(feedBackRequest.bookUuid())
                .orElseThrow(() -> new EntityNotFoundException("No book found with the uuid %s".formatted(feedBackRequest.bookUuid())));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback for an archived or not shareable book");
        }
        User user = ((User) connectedUser.getPrincipal());

        if(Objects.equals(book.getOwner().getUuid(), user.getUuid())) {
            throw new OperationNotPermittedException("You cannot give a feedback to your own book");
        }

        FeedBack feedBack = feedBackMapper.toFeedBack(feedBackRequest);
        var saveFeedBack = feedBackRepository.save(feedBack);

        return feedBackMapper.toFeedBackResponse(saveFeedBack, user.getUuid());
    }

    public PageResponse<FeedBackResponse> findAllFeedBacksByBook(
            UUID bookUuid, Integer page,
            Integer pageSize, Authentication connectedUser
    ) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        User user = ((User) connectedUser.getPrincipal());

        Page<FeedBack> feedBacks = feedBackRepository.findAllByBook(bookUuid, pageable);

        List<FeedBackResponse> feedBackResponses = feedBacks.stream().map(f -> feedBackMapper.toFeedBackResponse(f, user.getUuid())).toList();

        return new PageResponse<>(feedBackResponses, feedBacks.getNumber(),
                feedBacks.getSize(), feedBacks.getTotalElements(),
                feedBacks.getTotalPages(), feedBacks.isFirst(), feedBacks.isLast());
    }
}
