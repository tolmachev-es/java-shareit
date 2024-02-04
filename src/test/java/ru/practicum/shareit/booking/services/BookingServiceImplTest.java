package ru.practicum.shareit.booking.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.DBItemDao;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringJUnitConfig({BookingServiceImpl.class, DBUserDao.class, DBItemDao.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final TestEntityManager testEntityManager;
    private final UserDao userDao;
    private final ItemDao itemDao;

    @BeforeEach
    void create() {
        UserEntity user1 = new UserEntity();
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");
        userDao.create(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("Trinity");
        user2.setEmail("ilovetheone@matrix.com");
        userDao.create(user2);

        UserEntity user3 = new UserEntity();
        user3.setName("Morpheus");
        user3.setEmail("Morpheus@matrix.com");
        userDao.create(user3);

        ItemEntity itemDto = new ItemEntity();
        itemDto.setName("Red Pill");
        itemDto.setDescription("eat and wake up");
        itemDto.setAvailable(true);
        itemDto.setOwner(user1);
        itemDao.create(itemDto);
    }

    @Test
    @DirtiesContext
    void createNewBooking() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.MIN);
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingService.create(bookingDtoRequest, 2L);
        TypedQuery<BookingEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select b from BookingEntity as b where id = :id", BookingEntity.class);
        BookingEntity entity = query.setParameter("id", 1L).getSingleResult();
        assertThat(entity.getBooker().getId(), equalTo(2L));
        assertThat(entity.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(entity.getItem().getId(), equalTo(1L));
        assertThat(entity.getStart(), equalTo(LocalDateTime.MIN));
        assertThat(entity.getEnd(), equalTo((LocalDateTime.MAX)));
    }

    @Test
    @DirtiesContext
    void getByOwner() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.MIN);
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingService.create(bookingDtoRequest, 2L);
        List<BookingDtoResponse> responseList =
                new ArrayList<>(bookingService
                        .getByOwner("WAITING", 1L, 0, 20));
        assertThat(responseList.size(), equalTo(1));
        assertThat(responseList.get(0).getItem().getId(), equalTo(1L));

        List<BookingDtoResponse> responseList2 =
                new ArrayList<>(bookingService
                        .getByOwner("WAITING", 2L, 0, 20));
        assertThat(responseList2.size(), equalTo(0));


        List<BookingDtoResponse> responseListBooker1 =
                new ArrayList<>(bookingService
                        .getByBooker("WAITING", 2L, 0, 20));
        assertThat(responseListBooker1.size(), equalTo(1));
        assertThat(responseListBooker1.get(0).getItem().getId(), equalTo(1L));

        List<BookingDtoResponse> responseListBooker2 =
                new ArrayList<>(bookingService
                        .getByBooker("WAITING", 1L, 0, 20));
        assertThat(responseListBooker2.size(), equalTo(0));

        bookingService.approve(1L, 1L, true);
        TypedQuery<BookingEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select b from BookingEntity as b where id = :id", BookingEntity.class);
        BookingEntity entity = query.setParameter("id", 1L).getSingleResult();
        assertThat(entity.getStatus(), equalTo(BookingStatus.APPROVED));


        List<BookingDtoResponse> responseList2All =
                new ArrayList<>(bookingService
                        .getByOwner("ALL", 2L, 0, 20));
        assertThat(responseList2All.size(), equalTo(0));

        List<BookingDtoResponse> responseListBooker2All =
                new ArrayList<>(bookingService
                        .getByBooker("ALL", 1L, 0, 20));
        assertThat(responseListBooker2All.size(), equalTo(0));

        List<BookingDtoResponse> responseList2PAST =
                new ArrayList<>(bookingService
                        .getByOwner("PAST", 2L, 0, 20));
        assertThat(responseList2PAST.size(), equalTo(0));

        List<BookingDtoResponse> responseListBooker2PAST =
                new ArrayList<>(bookingService
                        .getByBooker("PAST", 1L, 0, 20));
        assertThat(responseListBooker2PAST.size(), equalTo(0));

        List<BookingDtoResponse> responseList2FUTURE =
                new ArrayList<>(bookingService
                        .getByOwner("FUTURE", 2L, 0, 20));
        assertThat(responseList2FUTURE.size(), equalTo(0));

        List<BookingDtoResponse> responseListBooker2FUTURE =
                new ArrayList<>(bookingService
                        .getByBooker("FUTURE", 1L, 0, 20));
        assertThat(responseListBooker2FUTURE.size(), equalTo(0));

        List<BookingDtoResponse> responseList2CURRENT =
                new ArrayList<>(bookingService
                        .getByOwner("CURRENT", 2L, 0, 20));
        assertThat(responseList2CURRENT.size(), equalTo(0));

        List<BookingDtoResponse> responseListBooker2CURRENT =
                new ArrayList<>(bookingService
                        .getByBooker("CURRENT", 1L, 0, 20));
        assertThat(responseListBooker2CURRENT.size(), equalTo(0));

        List<BookingDtoResponse> responseListBooker3CURRENT =
                new ArrayList<>(bookingService
                        .getByOwner("CURRENT", 3L, 0, 20));
        assertThat(responseListBooker3CURRENT.size(), equalTo(0));
    }

    @Test
    @DirtiesContext
    void getById() {
        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setItemId(1L);
        bookingDtoRequest.setStart(LocalDateTime.MIN);
        bookingDtoRequest.setEnd(LocalDateTime.MAX);
        bookingService.create(bookingDtoRequest, 2L);

        BookingDtoResponse response = bookingService.get(1L, 2L);

        assertThat(response.getId(), equalTo(1L));
    }

}
