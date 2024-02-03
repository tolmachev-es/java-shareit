package ru.practicum.shareit.booking.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.DBItemDao;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SpringJUnitConfig
class BookingServiceImplTestMock {
    private static BookingService bookingService;
    private static ItemDao itemDao;
    private static UserDao userDao;
    private static BookingRepository bookingRepository;

    @BeforeAll
    static void init() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        itemDao = Mockito.mock(DBItemDao.class);
        userDao = Mockito.mock(DBUserDao.class);
        bookingService = new BookingServiceImpl(userDao, itemDao, bookingRepository);
    }


    @Test
    void getNotAvailableItem() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Neo");
        user.setEmail("theone@matrix.com");

        ItemEntity item = new ItemEntity();
        item.setId(1L);
        item.setName("Red pill");
        item.setAvailable(false);
        item.setDescription("Take and wake up");
        item.setOwner(user);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setStart(LocalDateTime.MIN);
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingDtoRequest.setItemId(1L);

        Mockito.when(itemDao.get(Mockito.anyLong()))
                .thenReturn(item);

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> bookingService.create(bookingDtoRequest, 2L));

        assertThat(invalidParameterException.getMessage(),
                equalTo(String.format("Вещь с id %s недоступна для бронирования", 1L)));
    }

    @Test
    void ownerEqualBooker() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Neo");
        user.setEmail("theone@matrix.com");
        ItemEntity item = new ItemEntity();
        item.setId(1L);
        item.setName("Red pill");
        item.setAvailable(true);
        item.setDescription("Take and wake up");
        item.setOwner(user);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setStart(LocalDateTime.MIN);
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingDtoRequest.setItemId(1L);

        Mockito.when(itemDao.get(Mockito.anyLong()))
                .thenReturn(item);

        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> bookingService.create(bookingDtoRequest, 1L));

        assertThat(objectNotFound.getMessage(), equalTo("Владелец не может бронировать свою вещь"));
    }

    @Test
    void getBooking() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Neo");
        user.setEmail("theone@matrix.com");

        UserEntity user1 = new UserEntity();
        user1.setId(2L);
        user1.setName("Neo1");
        user1.setEmail("theone1@matrix.com");

        ItemEntity item = new ItemEntity();
        item.setId(1L);
        item.setName("Red pill");
        item.setAvailable(true);
        item.setDescription("Take and wake up");
        item.setOwner(user);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setEnd(LocalDateTime.MAX);
        bookingEntity.setStart(LocalDateTime.MIN);
        bookingEntity.setItem(item);
        bookingEntity.setBooker(user1);
        bookingEntity.setStatus(BookingStatus.WAITING);

        Optional<BookingEntity> bookingEntity1 = Optional.of(bookingEntity);

        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(bookingEntity1);

        BookingEntity bookingEntity2 = new BookingEntity();
        bookingEntity2.setId(1L);
        bookingEntity2.setEnd(LocalDateTime.MAX);
        bookingEntity2.setStart(LocalDateTime.MIN);
        bookingEntity2.setItem(item);
        bookingEntity2.setBooker(user1);
        bookingEntity2.setStatus(BookingStatus.APPROVED);

        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(bookingEntity2);

        BookingDtoResponse bookingDtoResponse = bookingService.approve(1L, 1L, true);

        assertThat(bookingDtoResponse.getId(), equalTo(1L));
        assertThat(bookingDtoResponse.getStatus(), equalTo(BookingStatus.APPROVED));
        assertThat(bookingDtoResponse.getItem().getId(), equalTo(1L));
        assertThat(bookingDtoResponse.getBooker().getId(), equalTo(2L));
    }

}
