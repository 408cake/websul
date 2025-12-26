package com.web.bookstore.book;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(length = 100)
    private String publisher;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(nullable = false)
    private Integer inventory;

    @Column(name = "average_rating", precision = 3, scale = 2, insertable = false, updatable = false)
    private BigDecimal averageRating;

    @Column(name = "review_count", insertable = false, updatable = false)
    private Integer reviewCount;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
