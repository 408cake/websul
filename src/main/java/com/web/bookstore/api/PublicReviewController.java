package com.web.bookstore.api;

import com.web.bookstore.*;
import com.web.bookstore.review.*;
import com.web.bookstore.api.dto.ReviewRequest;
import com.web.bookstore.common.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/reviews")
public class PublicReviewController {

    private final ReviewRepository reviewRepository;

    public PublicReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public ApiResponse<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,DESC") String sort,
            @RequestParam(required = false) Long bookId
    ) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(s[1]), s[0]));

        Page<Review> result = (bookId == null)
                ? reviewRepository.findAll(pageable)
                : reviewRepository.findByBookId(bookId, pageable);

        return ApiResponse.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> get(@PathVariable Long id) {
        return ApiResponse.ok(
                reviewRepository.findById(id)
                        .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND))
        );
    }

    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid ReviewRequest req) {
        Review r = new Review();
        r.setBookId(req.bookId());
        r.setUserId(req.userId());
        r.setRating(req.rating());
        r.setContent(req.content());
        return ApiResponse.ok(reviewRepository.save(r));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody @Valid ReviewRequest req) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        r.setRating(req.rating());
        r.setContent(req.content());
        return ApiResponse.ok(reviewRepository.save(r));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        reviewRepository.delete(r);
        return ApiResponse.ok(null);
    }
}
