package com.project.FirstProject.repositories;

import com.project.FirstProject.domain.entities.AuthorEntity;
import com.project.FirstProject.domain.entities.BookEntity;
import com.project.FirstProject.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {

    private BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        BookEntity book = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(book);
        Optional<BookEntity> result = underTest.findById(book.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled(){
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        BookEntity bookA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookA);

        BookEntity bookB = TestDataUtil.createTestBookB(authorEntity);
        underTest.save(bookB);

        BookEntity bookC = TestDataUtil.createTestBookC(authorEntity);
        underTest.save(bookC);

        Iterable<BookEntity> books = underTest.findAll();
        assertThat(books).hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void testThatBookCanBeUpdated(){
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        BookEntity bookA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookA);

        bookA.setTitle("UPDATED");

        underTest.save(bookA);

        Optional<BookEntity> result = underTest.findById(bookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookA);
    }

    @Test
    public void testThatBookCanBeDeleted(){
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();

        BookEntity bookA = TestDataUtil.createTestBookEntityA(authorEntity);
        underTest.save(bookA);

        underTest.deleteById(bookA.getIsbn());

        Optional<BookEntity> result = underTest.findById(bookA.getIsbn());

        assertThat(result).isEmpty();
    }
}