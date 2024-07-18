package com.project.FirstProject.repositories;

import com.project.FirstProject.domain.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends CrudRepository<BookEntity, String>{
}
