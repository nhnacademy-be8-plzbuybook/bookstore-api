package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "book_publisher_id", nullable = false)
    private Publisher publisher;

    @Column(nullable = false, length = 150)
    private String bookTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bookIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bookDescription;

    @Column(nullable = false)
    private LocalDate bookPubDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bookPriceStandard;

    @Column(nullable = false, length = 30, unique = true)
    private String bookIsbn;

    @Column(nullable = false, length = 30, unique = true)
    private String bookIsbn13;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellingBook> sellingBooks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
}
