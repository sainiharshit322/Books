package com.project.FirstProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.FirstProject.TestDataUtil;
import com.project.FirstProject.domain.dto.AuthorDto;
import com.project.FirstProject.domain.entities.AuthorEntity;
import com.project.FirstProject.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private AuthorService authorService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturns201HttpCreated() throws Exception {
        AuthorEntity authorA = TestDataUtil.createtestauthorA();
        authorA.setId(null);
        String authorJSON = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createtestauthorA();
        authorA.setId(null);
        String authorJSON = objectMapper.writeValueAsString(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatListAuthorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsHttpStatus404WhenNoAuthorExists() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/3")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetAuthorsReturnsAuthorWhenAuthorExists() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createtestauthorA();
        authorService.save(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatFullUpdateAuthorsReturnsHttpStatus404WhenAuthorDoesNotExists() throws Exception {
        AuthorDto authorDtoA = TestDataUtil.createtestauthorDtoA();
        String authorDtoJSON = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFullUpdateAuthorsReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntityA);

        AuthorDto authorDto = TestDataUtil.createtestauthorDtoA();
        String authorDtoJSON = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntityA);

        AuthorDto authorDto = TestDataUtil.createtestauthorDtoB();
        authorDto.setId(savedAuthor.getId());
        String authorDtoUpdateJSON = objectMapper.writeValueAsString(authorDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDto.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200Ok() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntityA);

        AuthorDto authorDto = TestDataUtil.createtestauthorDtoA();
        authorDto.setName("UPDATED");
        String authorDtoJSON = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntityA);

        AuthorDto authorDto = TestDataUtil.createtestauthorDtoA();
        authorDto.setName("UPDATED");
        String authorDtoJSON = objectMapper.writeValueAsString(authorDto);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/" + savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(savedAuthor.getAge())
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsGeneratesHttpStatus204ForNonExistingAuthor() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteAuthorReturnsGeneratesHttpStatus204ForExistingAuthor() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createtestauthorA();
        AuthorEntity savedAuthor = authorService.save(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/" + authorEntityA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
