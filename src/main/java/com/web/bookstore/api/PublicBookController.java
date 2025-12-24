package com.web.bookstore.api;

import com.web.bookstore.Item;
import com.web.bookstore.ItemRepository;
import com.web.bookstore.api.dto.BookSummary;
import com.web.bookstore.common.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/books")
public class PublicBookController {

    private final ItemRepository itemRepository;

    public PublicBookController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ApiResponse<PageResponse<BookSummary>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,DESC") String sort
    ) {
        if (page < 0 || size <= 0 || size > 100) {
            throw new ApiException(ErrorCode.INVALID_QUERY_PARAM, "page>=0, 1<=size<=100");
        }

        String[] s = sort.split(",");
        String sortField = s[0];
        Sort.Direction dir = (s.length > 1 && "ASC".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pr = PageRequest.of(page, size, Sort.by(dir, sortField));
        Page<Item> result = itemRepository.findAll(pr);

        Page<BookSummary> mapped = result.map(i -> new BookSummary(i.getId(), i.getTitle(), i.getPrice()));

        return ApiResponse.ok(PageResponse.from(mapped));
    }

    @GetMapping("/{id}")
    public ApiResponse<BookSummary> detail(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "book_id=" + id));

        return ApiResponse.ok(new BookSummary(item.getId(), item.getTitle(), item.getPrice()));
    }
}
