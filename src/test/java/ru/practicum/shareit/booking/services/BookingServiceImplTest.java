package ru.practicum.shareit.booking.services;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
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
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringJUnitConfig
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareIt"})
@ComponentScan(basePackages = {"ru.practicum.shareIt"})
class BookingServiceImplTest {
    private final BookingServiceImpl bookingService;
    private final TestEntityManager testEntityManager;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Test
    @DirtiesContext
    void createAndApprove() {
        UserEntity userOwner = new UserEntity();
        userOwner.setName("Owner");
        userOwner.setEmail("owner@mock.com");
        userRepository.save(userOwner);

        UserEntity userBooker = new UserEntity();
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

        Set<BookingDtoResponse> bookings = bookingService.getByOwner("ALL", 1L, 0, 10);
        assertThat(bookings.size(), equalTo(1));
    }
}