package ru.practicum.shareit.item.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemServiceImpl itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ItemDto itemDto;
    private ItemDto itemDto1;
    private CommentDto commentDto;
    private CommentDto commentDto1;

    @BeforeEach
    void init() {
        itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setName("Red pill");
        itemDto.setDescription("take and wake up");

        itemDto1 = new ItemDto();
        itemDto1.setId(1L);
        itemDto1.setAvailable(true);
        itemDto1.setName("Red pill");
        itemDto1.setDescription("take and wake up");

        commentDto = new CommentDto();
        commentDto.setText("Im wake up");
        commentDto.setCreated(LocalDateTime.MIN);
        commentDto.setAuthorName("NEO");

        commentDto1 = new CommentDto();
        commentDto1.setText("Im wake up");
        commentDto1.setId(1L);
        commentDto1.setCreated(LocalDateTime.MIN);
        commentDto1.setAuthorName("NEO");
    }

    @Test
    void createItem() throws Exception {
        given(itemService.create(Mockito.any(), Mockito.anyLong()))
                .willReturn(itemDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateItem() throws Exception {
        given(itemService.update(Mockito.any(), Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(itemDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getItem() throws Exception {
        given(itemService.get(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(itemDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllItem() throws Exception {
        given(itemService.getAllByOwner(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Set.of(itemDto1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void searchItem() throws Exception {
        given(itemService.search(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Set.of(itemDto1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/items?text=pill")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addComment() throws Exception {
        given(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .willReturn(commentDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
