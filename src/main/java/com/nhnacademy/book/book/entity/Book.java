package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Book {

    public Book(Publisher publisher, String bookTitle, String bookIndex, String bookDescription,
                LocalDate bookPubDate, BigDecimal bookPriceStandard, String bookIsbn13) {
        this.publisher = publisher;
        this.bookTitle = bookTitle;
        this.bookIndex = bookIndex;
        this.bookDescription = bookDescription;
        this.bookPubDate = bookPubDate;
        this.bookPriceStandard = bookPriceStandard;
        this.bookIsbn13 = bookIsbn13;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
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

    @Column(nullable = false, length = 40, unique = true)
    private String bookIsbn13;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellingBook> sellingBooks = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookCategory> bookCategories = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @Override
    public String toString() {
        return "bookId : " +
                bookId + ", bookTitle : "
                + bookTitle+ '\'' +
                '}';
    }
    // addCategory 메서드 추가
    public void addCategory(Category category) {
        // 중간 엔티티 생성
        BookCategory bookCategory = new BookCategory(this, category);

        // 양쪽 관계 동기화
        this.bookCategories.add(bookCategory);
        category.getBookCategories().add(bookCategory);
    }
}