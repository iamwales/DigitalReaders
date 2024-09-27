package com.wales.book_socials.feedback;

import com.wales.book_socials.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedBackController {
    private final FeedBackService feedBackService;

    @PostMapping
    public ResponseEntity<FeedBackResponse> saveFeedBack(
            @Valid @RequestBody FeedBackRequest feedBackRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedBackService.saveFeedBack(feedBackRequest, connectedUser));
    }

    @GetMapping("/book/{bookUuid}")
    public ResponseEntity<PageResponse<FeedBackResponse>> getAllFeedBacksByBook(
            @PathVariable("bookUuid") UUID bookUuid,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedBackService.findAllFeedBacksByBook(bookUuid, page, pageSize, connectedUser));
    }

}
