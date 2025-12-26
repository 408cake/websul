package com.web.bookstore.api;

import com.web.bookstore.category.Category;
import com.web.bookstore.category.CategoryRepository;
import com.web.bookstore.api.dto.CategoryRequest;
import com.web.bookstore.common.ApiException;
import com.web.bookstore.common.ApiResponse;
import com.web.bookstore.common.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    public AdminCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping("/{id}")
    public ApiResponse<?> getOne(@PathVariable Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        return ApiResponse.ok(c);
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
