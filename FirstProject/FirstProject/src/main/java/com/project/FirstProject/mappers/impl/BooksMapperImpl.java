package com.project.FirstProject.mappers.impl;

import com.project.FirstProject.domain.dto.BookDto;
import com.project.FirstProject.domain.entities.BookEntity;
import com.project.FirstProject.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BooksMapperImpl implements Mapper<BookEntity, BookDto> {

    private ModelMapper modelMapper;

    public BooksMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapto(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
