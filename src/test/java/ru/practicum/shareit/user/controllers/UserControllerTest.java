package ru.practicum.shareit.user.controllers;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private UserDto userDtoBefore;
    private UserDto userDtoAfter;

    @BeforeEach
    void init() {
        userDtoBefore = new UserDto();
        userDtoBefore.setEmail("theone@matrix.ru");
        userDtoBefore.setName("NEO");

        userDtoAfter = new UserDto();
        userDtoAfter.setId(1L);
        userDtoAfter.setEmail("theone@matrix.ru");
        userDtoAfter.setName("NEO");
    }

    @Test
    void createNewUser() throws Exception {
        given(userService.create(Mockito.any()))
                .willReturn(userDtoAfter);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(userDtoBefore))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateUser() throws Exception {
        given(userService.update(Mockito.any(), Mockito.anyLong()))
                .willReturn(userDtoAfter);
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/{id}", 1L)
                        .content(objectMapper.writeValueAsString(userDtoBefore))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getUserById() throws Exception {
        given(userService.getById(Mockito.anyLong()))
                .willReturn(userDtoAfter);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllUser() throws Exception {
        given(userService.getAll())
                .willReturn(Set.of(userDtoAfter));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{id}", 1L))
                .andExpect(status().is2xxSuccessful());
    }
}
