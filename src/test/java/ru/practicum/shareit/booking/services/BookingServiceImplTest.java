package ru.practicum.shareit.booking.services;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.item.dao.item.ItemRepository;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringJUnitConfig
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@ComponentScan(basePackages = {"ru.practicum.shareit"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)//без classMode отказывается один из тестов пассить
class BookingServiceImplTest {
    private final BookingService bookingService;
    private final TestEntityManager testEntityManager;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private UserEntity userOwner;
    private UserEntity userBooker;
    private final BookingRepository bookingRepository;

    @Test
    void createAndApprove() {
        userOwner = new UserEntity();
        userOwner.setName("Owner");
        userOwner.setEmail("owner@mock.com");
        userRepository.save(userOwner);

        userBooker = new UserEntity();
        userBooker.setName("Booker");
        userBooker.setEmail("booker@mock.com");
        userRepository.save(userBooker);

        ItemEntity item = new ItemEntity();
        item.setName("knife");
        item.setDescription("Knife");
        item.setOwner(userOwner);
        item.setAvailable(true);
        itemRepository.save(item);

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingDtoRequest.setItemId(1L);
        bookingService.create(bookingDtoRequest, 2L);

        TypedQuery<BookingEntity> query = testEntityManager.getEntityManager()
                .createQuery("select b from BookingEntity as b where b.id = :id", BookingEntity.class);
        BookingEntity bookingEntity = query.setParameter("id", 1L).getSingleResult();
        assertThat(bookingEntity.getEnd(), Matchers.notNullValue());
        assertThat(bookingEntity.getStart(), Matchers.notNullValue());
        assertThat(bookingEntity.getBooker().getId(), equalTo(2L));
        assertThat(bookingEntity.getStatus(), equalTo(BookingStatus.WAITING));

        bookingService.approve(1L, 1L, true);
        BookingEntity afterApprove = query.setParameter("id", 1L).getSingleResult();
        assertThat(afterApprove.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void testSorting() {
        userOwner = new UserEntity();
        userOwner.setName("Owner");
        userOwner.setEmail("owner@mock.com");
        userRepository.save(userOwner);

        userBooker = new UserEntity();
        userBooker.setName("Booker");
        userBooker.setEmail("booker@mock.com");
        userRepository.save(userBooker);

        ItemEntity item = new ItemEntity();
        item.setName("knife");
        item.setDescription("Knife");
        item.setOwner(userOwner);
        item.setAvailable(true);
        itemRepository.save(item);

        BookingEntity bookingPrevious = new BookingEntity();
        bookingPrevious.setId(1L);
        bookingPrevious.setItem(item);
        bookingPrevious.setBooker(userBooker);
        bookingPrevious.setStart(LocalDateTime.now().minusDays(4));
        bookingPrevious.setEnd(LocalDateTime.now().minusDays(3));
        bookingPrevious.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(bookingPrevious);

        BookingEntity bookingNext = new BookingEntity();
        bookingNext.setId(2L);
        bookingNext.setItem(item);
        bookingNext.setBooker(userBooker);
        bookingNext.setStart(LocalDateTime.now().plusDays(4));
        bookingNext.setEnd(LocalDateTime.now().plusDays(5));
        bookingNext.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(bookingNext);

        BookingEntity bookingCurrent = new BookingEntity();
        bookingCurrent.setId(3L);
        bookingCurrent.setItem(item);
        bookingCurrent.setBooker(userBooker);
        bookingCurrent.setStart(LocalDateTime.now().minusDays(1));
        bookingCurrent.setEnd(LocalDateTime.now().plusDays(1));
        bookingCurrent.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(bookingCurrent);

        BookingEntity bookingRejected = new BookingEntity();
        bookingRejected.setId(5L);
        bookingRejected.setItem(item);
        bookingRejected.setBooker(userBooker);
        bookingRejected.setStart(LocalDateTime.now().plusDays(7));
        bookingRejected.setEnd(LocalDateTime.now().plusDays(8));
        bookingRejected.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(bookingRejected);

        List<BookingDtoResponse> getPreviousByOwner = new ArrayList<>(
                bookingService.getByOwner("PAST", 1L, 0, 10));

        assertThat(getPreviousByOwner.size(), equalTo(1));
        assertThat(getPreviousByOwner.get(0).getId(), equalTo(1L));

        List<BookingDtoResponse> getCurrentByOwner = new ArrayList<>(
                bookingService.getByOwner("CURRENT", 1L, 0, 10));

        assertThat(getCurrentByOwner.size(), equalTo(1));
        assertThat(getCurrentByOwner.get(0).getId(), equalTo(3L));

        List<BookingDtoResponse> getFutureByOwner = new ArrayList<>(
                bookingService.getByOwner("FUTURE", 1L, 0, 10));

        assertThat(getFutureByOwner.size(), equalTo(2));
        assertThat(getFutureByOwner.get(0).getId(), equalTo(2L));
        assertThat(getFutureByOwner.get(1).getId(), equalTo(4L));

        List<BookingDtoResponse> getRejectedByOwner = new ArrayList<>(
                bookingService.getByOwner("REJECTED", 1L, 0, 10));

        assertThat(getRejectedByOwner.size(), equalTo(1));
        assertThat(getRejectedByOwner.get(0).getId(), equalTo(4L));

        List<BookingDtoResponse> getPreviousByBooker = new ArrayList<>(
                bookingService.getByBooker("PAST", 2L, 0, 10));

        assertThat(getPreviousByBooker.size(), equalTo(1));
        assertThat(getPreviousByBooker.get(0).getId(), equalTo(1L));

        List<BookingDtoResponse> getCurrentByBooker = new ArrayList<>(
                bookingService.getByBooker("CURRENT", 2L, 0, 10));

        assertThat(getCurrentByBooker.size(), equalTo(1));
        assertThat(getCurrentByBooker.get(0).getId(), equalTo(3L));

        List<BookingDtoResponse> getFutureByBooker = new ArrayList<>(
                bookingService.getByBooker("FUTURE", 2L, 0, 10));

        assertThat(getFutureByBooker.size(), equalTo(2));
        assertThat(getFutureByBooker.get(0).getId(), equalTo(2L));
        assertThat(getFutureByBooker.get(1).getId(), equalTo(4L));

        List<BookingDtoResponse> getRejectedByBooker = new ArrayList<>(
                bookingService.getByBooker("REJECTED", 2L, 0, 10));

        assertThat(getRejectedByBooker.size(), equalTo(1));
        assertThat(getRejectedByBooker.get(0).getId(), equalTo(4L));


    }
}