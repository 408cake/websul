package com.web.bookstore.api;

import com.web.bookstore.author.*;
import com.web.bookstore.api.dto.AuthorRequest;
import com.web.bookstore.common.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/authors")
public class PublicAuthorController {

    private final AuthorRepository authorRepository;

    public PublicAuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public ApiResponse<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,DESC") String sort,
            @RequestParam(required = false) String keyword
    ) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(s[1]), s[0]));

        Page<Author> result = (keyword == null || keyword.isBlank())
                ? authorRepository.findAll(pageable)
                : authorRepository.findByNameContainingIgnoreCase(keyword, pageable);

        return ApiResponse.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> get(@PathVariable Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        return ApiResponse.ok(author);
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
