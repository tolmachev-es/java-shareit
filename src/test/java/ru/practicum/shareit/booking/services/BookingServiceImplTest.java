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
    }
}
