package com.project.FirstProject.repositories;

import com.project.FirstProject.TestDataUtil;
import com.project.FirstProject.domain.entities.AuthorEntity;
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
public class AuthorEntityRepositoryIntegrationTests {

    private AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }
    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(authorEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled(){
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createtestauthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createtestauthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAll();
        assertThat(result).hasSize(3);
        assertThat(result).contains(authorEntityA, authorEntityB, authorEntityC);
    }

    @Test
    public void testThatAuthorCanBeUpdated(){
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        underTest.save(authorEntityA);
        authorEntityA.setName("UPDATED");
        underTest.save(authorEntityA);
        Optional<AuthorEntity> result = underTest.findById(authorEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorEntityA);
    }

    @Test
    public void testThatAuthorCanBeDeleted(){
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        underTest.save(authorEntityA);

        underTest.deleteById(authorEntityA.getId());

        Optional<AuthorEntity> result = underTest.findById(authorEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan(){
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createtestauthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createtestauthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);
        assertThat(result).containsExactly(authorEntityB, authorEntityC);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan(){
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createtestauthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createtestauthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgeGreaterThan(50);
        assertThat(result).containsExactly(authorEntityA);
    }
}