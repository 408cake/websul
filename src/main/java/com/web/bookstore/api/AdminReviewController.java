package com.web.bookstore.api;

import com.web.bookstore.review.Review;
import com.web.bookstore.review.ReviewRepository;
import com.web.bookstore.api.dto.ReviewRequest;
import com.web.bookstore.common.ApiException;
import com.web.bookstore.common.ApiResponse;
import com.web.bookstore.common.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewRepository reviewRepository;

    public AdminReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    @GetMapping("/{id}")
    public ApiResponse<?> getOne(@PathVariable Long id) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        return ApiResponse.ok(r);
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
