package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class BookingDtoResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingState status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDtoResponse that = (BookingDtoResponse) o;
        return Objects.equals(that.id, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(-id);
    }
}
