package com.nhnacademy.book.booktest.service;

import com.nhnacademy.book.book.entity.Author;
import com.nhnacademy.book.book.repository.AuthorRepository;
import com.nhnacademy.book.book.service.Impl.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
public class AuthorServiceTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    List<Author> authorList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Author author1 = new Author();
        author1.setAuthorName("test1");

        Author author2 = new Author();
        author2.setAuthorName("test2");

        Author author3 = new Author();
        author3.setAuthorName("test3");

        Author author4 = new Author();
        author4.setAuthorName("test4");

        authorList.add(author1);
        authorList.add(author2);
        authorList.add(author3);
        authorList.add(author4);

        // 여러 Author 객체에 대해 save()가 호출될 때마다 mock 처리
        Mockito.when(authorRepository.save(Mockito.any(Author.class)))
                .thenReturn(author1)
                .thenReturn(author2)
                .thenReturn(author3)
                .thenReturn(author4);

    }

    @Test
    void createAuthor() {

        Author author = new Author();
        author.setAuthorName("test_created");
        Mockito.when(authorRepository.save(author)).thenReturn(author);

        Author author1 = authorService.createAuthor(author);
        assertThat(author1.getAuthorName()).isEqualTo("test_created");

    }

    @Test
    void getAllAuthors() {

        Mockito.when(authorRepository.findAll()).thenReturn(authorList);

        List<Author> authors = authorService.getAllAuthors();

        assertThat(authors.size()).isEqualTo(4);

    }

    @Test
    void getAuthorById() {

        Author author = new Author();
        author.setAuthorName("test_created");

        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.of(author));
        Author author1 = authorService.getAuthorById(5L);
        assertThat(author1.getAuthorName()).isEqualTo("test_created");
    }

    @Test
    void deleteAuthorById() {
        Author author = new Author();
        author.setAuthorName("test_created");
        Mockito.when(authorRepository.findById(5L)).thenReturn(Optional.of(author));
        authorService.deleteAuthorById(5L);
        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(5L);

    }





}