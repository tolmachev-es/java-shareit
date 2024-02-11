package ru.practicum.shareit.booking.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dto.BookingDtoByItemRequest;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;

@Mapper
public interface BookingMapper {
    BookingMapper BOOKING_MAPPER = Mappers.getMapper(BookingMapper.class);

    BookingDtoResponse toDto(Booking booking);

    Booking fromDto(BookingDtoRequest bookingDtoRequest);

    BookingEntity toEntity(Booking booking);

    Booking fromEntity(BookingEntity bookingEntity);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingDtoByItemRequest toDtoByItemRequest(Booking booking);
}
