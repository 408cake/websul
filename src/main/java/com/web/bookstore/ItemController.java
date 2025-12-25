package com.web.bookstore;

import com.web.bookstore.api.dto.ItemCreateRequest;
import com.web.bookstore.api.dto.ItemUpdateRequest;
import com.web.bookstore.common.ApiResponse;
import com.web.bookstore.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/items")
public class ItemController {

    private final ItemRepository itemRepository;

    // 목록 + 검색 + 페이징/정렬
    @GetMapping
    public ApiResponse<PageResponse<Item>> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,DESC") String sort
    ) {
        Sort s = parseSort(sort);
        Pageable pageable = PageRequest.of(page, size, s);

        Page<Item> result = (keyword == null || keyword.isBlank())
                ? itemRepository.findAll(pageable)
                : itemRepository.findByTitleContainingIgnoreCase(keyword, pageable);

        return ApiResponse.ok(PageResponse.from(result));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ApiResponse<Item> get(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ITEM_NOT_FOUND"));
        return ApiResponse.ok(item);
    }

    // 생성 (일단 public로 열어뒀음. 나중에 admin으로 옮기면 됨)
    @PostMapping
    public ApiResponse<Item> create(@Valid @RequestBody ItemCreateRequest req) {
        Item item = Item.builder()
                .title(req.title())
                .price(req.price())
                .stock(req.stock())
                .build();

        Item saved = itemRepository.save(item);
        return ApiResponse.ok(saved);
    }

    // 수정
    @PutMapping("/{id}")
    public ApiResponse<Item> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest req
    ) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ITEM_NOT_FOUND"));

        item.update(req.title(), req.price(), req.stock());
        Item saved = itemRepository.save(item);

        return ApiResponse.ok(saved);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("ITEM_NOT_FOUND");
        }
        itemRepository.deleteById(id);
        return ApiResponse.ok(null);
    }

    private Sort parseSort(String sort) {
        // "id,DESC" 형태
        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction dir = (parts.length > 1 && "ASC".equalsIgnoreCase(parts[1]))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(dir, field);
    }
}
