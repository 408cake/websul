package com.web.bookstore.api;

import com.web.bookstore.author.Author;
import com.web.bookstore.author.AuthorRepository;
import com.web.bookstore.api.dto.AuthorRequest;
import com.web.bookstore.common.ApiException;
import com.web.bookstore.common.ApiResponse;
import com.web.bookstore.common.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/authors")
public class AdminAuthorController {

    private final AuthorRepository authorRepository;

    public AdminAuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/{id}")
    public ApiResponse<?> get(@PathVariable Long id) {
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        return ApiResponse.ok(a);
    }

    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid AuthorRequest req) {
        Author a = new Author();
        a.setName(req.name());
        return ApiResponse.ok(authorRepository.save(a));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody @Valid AuthorRequest req) {
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        a.setName(req.name());
        return ApiResponse.ok(authorRepository.save(a));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        authorRepository.delete(a);
        return ApiResponse.ok(null);
    }
}
