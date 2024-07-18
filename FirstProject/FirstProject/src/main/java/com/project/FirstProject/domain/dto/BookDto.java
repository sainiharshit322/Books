package com.project.FirstProject.domain.dto;

import com.project.FirstProject.domain.entities.AuthorEntity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private String isbn;

    private String title;

    private AuthorDto author;

}
