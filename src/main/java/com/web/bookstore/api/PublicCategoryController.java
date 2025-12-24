package com.web.bookstore.api;

import com.web.bookstore.*;
import com.web.bookstore.category.*;
import com.web.bookstore.api.dto.CategoryRequest;
import com.web.bookstore.common.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/categories")
public class PublicCategoryController {

    private final CategoryRepository categoryRepository;

    public PublicCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ApiResponse<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,DESC") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long parentId
    ) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(s[1]), s[0]));

        Page<Category> result;
        if (parentId != null) {
            result = categoryRepository.findByParentId(parentId, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            result = categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            result = categoryRepository.findAll(pageable);
        }

        return ApiResponse.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> get(@PathVariable Long id) {
        return ApiResponse.ok(
                categoryRepository.findById(id)
                        .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND))
        );
    }

    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid CategoryRequest req) {
        Category c = new Category();
        c.setName(req.name());
        c.setParentId(req.parentId());
        return ApiResponse.ok(categoryRepository.save(c));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody @Valid CategoryRequest req) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        c.setName(req.name());
        c.setParentId(req.parentId());
        return ApiResponse.ok(categoryRepository.save(c));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        categoryRepository.delete(c);
        return ApiResponse.ok(null);
    }
}
