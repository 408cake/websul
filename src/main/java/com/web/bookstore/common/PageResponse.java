package com.web.bookstore.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        PageMeta page
) {
    public static <T> PageResponse<T> from(Page<T> p) {
        List<SortMeta> sorts = p.getSort().stream()
                .map(o -> new SortMeta(o.getProperty(), o.getDirection().name()))
                .toList();

        return new PageResponse<>(
                p.getContent(),
                new PageMeta(
                        p.getNumber(),
                        p.getSize(),
                        p.getTotalElements(),
                        p.getTotalPages(),
                        p.hasNext(),
                        p.hasPrevious(),
                        sorts
                )
        );
    }

    public record PageMeta(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext,
            boolean hasPrev,
            List<SortMeta> sort
    ) { }

    public record SortMeta(
            String property,
            String direction
    ) { }
}
