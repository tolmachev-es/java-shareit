package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private ItemRequestDto itemRequestDto1;
    private ItemRequestDto itemRequestDto2;
    private ItemRequestDto itemRequestDto3;

    @BeforeEach
    void init() {
        itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setCreated(LocalDateTime.now());
        itemRequestDto1.setDescription("Red pill");

        itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(1L);
        itemRequestDto2.setCreated(LocalDateTime.now());
        itemRequestDto2.setDescription("Red pill");

        itemRequestDto3 = new ItemRequestDto();
        itemRequestDto3.setId(2L);
        itemRequestDto3.setCreated(LocalDateTime.now());
        itemRequestDto3.setDescription("Blue pill");
    }

    @Test
    void createItemRequest() throws Exception {
        given(itemRequestService.create(Mockito.any(), Mockito.anyLong()))
                .willReturn(itemRequestDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllByUserId() throws Exception {
        given(itemRequestService.get(Mockito.anyLong()))
                .willReturn(Set.of(itemRequestDto2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAll() throws Exception {
        given(itemRequestService.getWithPagination(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Set.of(itemRequestDto2, itemRequestDto3));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getById() throws Exception {
        given(itemRequestService.getById(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(itemRequestDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/requests/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
