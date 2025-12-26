package com.web.bookstore.api;

import com.web.bookstore.api.dto.BookUpsertRequest;
import com.web.bookstore.book.Book;
import com.web.bookstore.book.BookRepository;
import com.web.bookstore.common.ApiException;
import com.web.bookstore.common.ApiResponse;
import com.web.bookstore.common.ErrorCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

    private final BookRepository bookRepository;

    public AdminBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getOne(@PathVariable Long id) {
        Book b = bookRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        return ApiResponse.ok(b);
    }
    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid BookUpsertRequest req) {
        Book b = new Book();
        b.setSellerId(req.sellerId());
        b.setTitle(req.title());
        b.setIsbn(req.isbn());
        b.setPublisher(req.publisher());
        b.setSummary(req.summary());
        b.setPrice(req.price());
        b.setPublicationDate(req.publicationDate());
        b.setInventory(req.inventory());
        return ApiResponse.ok(bookRepository.save(b));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody @Valid BookUpsertRequest req) {
        Book b = bookRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        b.setSellerId(req.sellerId());
        b.setTitle(req.title());
        b.setIsbn(req.isbn());
        b.setPublisher(req.publisher());
        b.setSummary(req.summary());
        b.setPrice(req.price());
        b.setPublicationDate(req.publicationDate());
        b.setInventory(req.inventory());
        return ApiResponse.ok(bookRepository.save(b));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        Book b = bookRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND));
        bookRepository.delete(b);
        return ApiResponse.ok(null);
    }
}
