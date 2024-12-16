package com.nhnacademy.book.book.repository;

import com.nhnacademy.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByBookIsbn13(String bookIsbn13);

    List<Book> findByBookTitle(String bookTitle);

    List<Book> findByBookTitleContaining(String bookTitle);


//    // 책을 쓴 작가를 검색
//    @Query("SELECT a FROM Author a JOIN a.bookAuthors ba JOIN ba.book b WHERE b.bookId = :bookId")
//    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);
//
//    // 작가가 집필한 책 검색
//    @Query("SELECT b FROM Book b JOIN b.bookAuthors ba JOIN ba.author a WHERE a.authorName = :authorName")
//    List<Book> findBooksByAuthorName(@Param("authorName") String authorName);


}