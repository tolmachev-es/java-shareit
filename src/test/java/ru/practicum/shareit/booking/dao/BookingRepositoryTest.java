package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.HeadExeptions.AlreadyExistException;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.services.BookingServiceImpl;
import ru.practicum.shareit.item.dao.item.DBItemDao;
import ru.practicum.shareit.item.dao.item.ItemDao;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
class BookingRepositoryTest {
    private BookingServiceImpl bookingService;
    private BookingRepository bookingRepository;
    private ItemDao itemDao;
    private UserDao userDao;
    private ItemEntity item;
    private UserEntity owner;
    private BookingDtoRequest booking;
    private BookingEntity bookingEntity;
    private UserEntity booker;

    @BeforeEach
    void setUp() {
        itemDao = Mockito.mock(DBItemDao.class);
        userDao = Mockito.mock(DBUserDao.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(userDao, itemDao, bookingRepository);

        owner = new UserEntity();
        owner.setId(1L);
        owner.setName("neo");
        owner.setEmail("theone@mock.com");

        booker = new UserEntity();
        booker.setId(2L);
        booker.setName("trinity");
        booker.setEmail("trinity@mock.com");

        item = new ItemEntity();
        item.setId(1L);
        item.setName("Red pill");
        item.setDescription("take and wake up");
        item.setAvailable(true);
        item.setOwner(owner);

        bookingEntity = new BookingEntity();
        bookingEntity.setId(1L);
        bookingEntity.setStatus(BookingStatus.APPROVED);
        bookingEntity.setItem(item);
        bookingEntity.setStart(LocalDateTime.MIN);
        bookingEntity.setStart(LocalDateTime.now());
        bookingEntity.setBooker(booker);

        booking = new BookingDtoRequest();
        booking.setStart(LocalDateTime.MAX);
        booking.setEnd(LocalDateTime.MIN);
        booking.setItemId(1L);
    }

    @Test
    void ownerEqualBooker() {
        Mockito.when(itemDao.get(Mockito.anyLong()))
                .thenReturn(item);
        ObjectNotFound objectNotFound = assertThrows(
                ObjectNotFound.class, () -> bookingService.create(booking, 1L));

        assertThat(objectNotFound.getMessage(), equalTo("Владелец не может бронировать свою вещь"));
    }

    @Test
    void itemNotAvailable() {
        item.setAvailable(false);
        Mockito.when(itemDao.get(Mockito.anyLong()))
                .thenReturn(item);
        InvalidParameterException invalidParameterException = assertThrows(
                InvalidParameterException.class, () -> bookingService.create(booking, 2L));

        assertThat(invalidParameterException.getMessage(), equalTo("Вещь с id 1 недоступна для бронирования"));
    }

    @Test
    void startAfterEnd() {
        Mockito.when(itemDao.get(Mockito.anyLong()))
                .thenReturn(item);
        InvalidParameterException invalidParameterException = assertThrows(
                InvalidParameterException.class, () -> bookingService.create(booking, 2L));

        assertThat(invalidParameterException.getMessage(), equalTo("Ошибка во времени бронирования"));
    }

    @Test
    void statusAlreadyChange() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingEntity));
        AlreadyExistException alreadyExistException = assertThrows(
                AlreadyExistException.class, () -> bookingService.approve(1L, 1L, true));

        assertThat(alreadyExistException.getMessage(), equalTo("Статус уже изменен"));
    }

    @Test
    void bookingNotFound() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> bookingService.approve(1L, 1L, true));

        assertThat(objectNotFound.getMessage(), equalTo("Бронирование с id 1 не найдено"));
    }

    @Test
    void aproveAnotherOwner() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingEntity));
        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> bookingService.approve(1L, 2L, true));

        assertThat(objectNotFound.getMessage(),
                equalTo("Не найдено бронирование которое может подтвердить пользователь с id 2"));
    }

    @Test
    void getBookingNotFound() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> bookingService.get(1L, 2L));

        assertThat(objectNotFound.getMessage(), equalTo("Бронирование с id 1 не найдено"));
    }

    @Test
    void getBookingUserNotFoundInBookingRequest() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingEntity));
        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> bookingService.get(1L, 3L));

        assertThat(objectNotFound.getMessage(),
                equalTo("Пользователь с id 3 не найден среди участников бронирования с id 1"));
    }

    @Test
    void getBooking() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(bookingEntity));
        BookingDtoResponse bookingDtoResponse = bookingService.get(1L, 1L);
        assertThat(bookingDtoResponse.getId(), equalTo(1L));
        assertThat(bookingDtoResponse.getBooker().getName(), equalTo("trinity"));
    }

    @Test
    void getAllByOwnerIncorrect() {
        Mockito.when(userDao.getUserById(Mockito.anyLong())).thenReturn(owner);
        InvalidParameterException invalidParameterException = assertThrows(
                InvalidParameterException.class,
                () -> bookingService.getByOwner("NOW", 1L, 0, 10));
        assertThat(invalidParameterException, notNullValue());
    }

    @Test
    void getAllByBookerIncorrect() {
        Mockito.when(userDao.getUserById(Mockito.anyLong())).thenReturn(owner);
        InvalidParameterException invalidParameterException = assertThrows(
                InvalidParameterException.class,
                () -> bookingService.getByBooker("NOW", 1L, 0, 10));
        assertThat(invalidParameterException, notNullValue());
    }
}