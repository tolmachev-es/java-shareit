package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoByItemRequest;

import java.util.Objects;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingDtoByItemRequest lastBooking;
    private BookingDtoByItemRequest nextBooking;
    private Set<CommentDto> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
