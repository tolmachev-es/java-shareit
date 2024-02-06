package ru.practicum.shareit.booking.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.services.BookingServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingServiceImpl bookingService;
    @InjectMocks
    private BookingController controller;
    private MockMvc mockMvc;
    private BookingDtoRequest bookingDtoRequest;
    private BookingDtoResponse bookingDtoResponse;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        objectMapper.registerModule(new JavaTimeModule());
        bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.now().plusMinutes(2)));
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(1L);
        bookingDtoResponse.setStatus(BookingStatus.WAITING);
        bookingDtoResponse.setStart(LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(2)));
        bookingDtoResponse.setEnd(LocalDateTime.MAX);
    }

    @Test
    void createBooking() throws Exception {
        given(bookingService.create(Mockito.any(), Mockito.anyLong()))
                .willReturn(bookingDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createBookingEmptyItem() throws Exception {
        bookingDtoRequest.setItemId(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createBookingStartNull() throws Exception {
        bookingDtoRequest.setStart(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createBookingStartInPast() throws Exception {
        bookingDtoRequest.setStart(LocalDateTime.MIN);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createBookingEndNull() throws Exception {
        bookingDtoRequest.setEnd(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createBookingEndInPresent() throws Exception {
        bookingDtoRequest.setEnd(LocalDateTime.now());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getBookingById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void approvedWithoutBookingId() throws Exception {
        given(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .willReturn(bookingDtoResponse);
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bookings/{id}?approved=true", 1)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllByBooker() throws Exception {
        given(bookingService.getByBooker(Mockito.any(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Set.of(bookingDtoResponse));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllByOwner() throws Exception {
        given(bookingService.getByOwner(Mockito.any(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(Set.of(bookingDtoResponse));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }


}
