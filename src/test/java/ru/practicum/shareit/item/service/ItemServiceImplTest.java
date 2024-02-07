package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.comment.CommentEntity;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@Transactional
@SpringJUnitConfig
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class ItemServiceImplTest {
    private final ItemService itemService;
    private final ItemRequestRepository itemRequestRepository;
    private final UserDao userDao;
    private final BookingRepository bookingRepository;
    private final TestEntityManager testEntityManager;

    @Test
    @DirtiesContext
    void getAllWithBookingAndComment() {
        UserEntity user = new UserEntity();
        user.setName("Neo");
        user.setEmail("theone@mock.com");
        userDao.create(user);

        UserEntity userBooker = new UserEntity();
        userBooker.setName("Booker");
        userBooker.setEmail("Booker@mock.com");
        userDao.create(userBooker);

        ItemRequestEntity request = new ItemRequestEntity();
        request.setRequestor(userBooker);
        request.setCreated(LocalDateTime.MIN);
        request.setDescription("Daite tablov");
        itemRequestRepository.save(request);

        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(1L);
        itemDto.setName("Pill");
        itemDto.setDescription("Red pill");
        itemDto.setAvailable(true);
        itemService.create(itemDto, 1L);

        Set<ItemDto> search = itemService.search("Pill", 0, 10);
        assertThat(search.size(), equalTo(1));

        TypedQuery<ItemEntity> itemEntityTypedQuery = testEntityManager
                .getEntityManager()
                .createQuery("select i from ItemEntity as i where i.id = :id", ItemEntity.class);
        ItemEntity item = itemEntityTypedQuery.setParameter("id", 1L).getSingleResult();
        assertThat(item.getOwner(), equalTo(user));
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getItemRequest(), equalTo(request));

        BookingEntity previousBooking = new BookingEntity();
        previousBooking.setBooker(userBooker);
        previousBooking.setItem(item);
        previousBooking.setStatus(BookingStatus.APPROVED);
        previousBooking.setStart(LocalDateTime.now().minusDays(3));
        previousBooking.setEnd(LocalDateTime.now().minusDays(2));
        bookingRepository.save(previousBooking);

        BookingEntity nextBooking = new BookingEntity();
        nextBooking.setBooker(userBooker);
        nextBooking.setItem(item);
        nextBooking.setStatus(BookingStatus.APPROVED);
        nextBooking.setStart(LocalDateTime.now().plusDays(1));
        nextBooking.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(nextBooking);

        CommentDto comment = new CommentDto();
        comment.setText("good pill");
        comment.setAuthorName("name");
        itemService.addComment(2L, 1L, comment);
        TypedQuery<CommentEntity> commentEntityTypedQuery = testEntityManager
                .getEntityManager()
                .createQuery("select c from CommentEntity as c where c.id = :id", CommentEntity.class);
        CommentEntity commentEntity = commentEntityTypedQuery.setParameter("id", 1L).getSingleResult();
        assertThat(commentEntity.getAuthor(), equalTo(userBooker));
        assertThat(commentEntity.getItem(), equalTo(item));

        Set<ItemDto> getItems = itemService.getAllByOwner(1L, 0, 10);
        assertThat(getItems.size(), equalTo(1));
        ItemDto getItem = new ArrayList<>(getItems).get(0);
        assertThat(getItem.getId(), equalTo(1L));
        assertThat(getItem.getLastBooking().getId(), equalTo(1L));
        assertThat(getItem.getNextBooking().getId(), equalTo(2L));
        assertThat(getItem.getComments().size(), equalTo(1));

        ItemDto getItemByBooker = itemService.get(1L, 2L);
        assertThat(getItemByBooker.getLastBooking(), nullValue());
        assertThat(getItemByBooker.getNextBooking(), nullValue());
        assertThat(getItemByBooker.getComments().size(), equalTo(1));

        ItemDto itemDtoUp = new ItemDto();
        itemDtoUp.setRequestId(1L);
        itemDtoUp.setName("Pill1");
        itemDtoUp.setDescription("Red pill1");
        itemDtoUp.setAvailable(false);
        itemService.update(itemDtoUp, 1L, 1L);

        TypedQuery<ItemEntity> itemUpdate = testEntityManager
                .getEntityManager()
                .createQuery("select i from ItemEntity as i where i.id = :id", ItemEntity.class);
        ItemEntity itemUp = itemUpdate.setParameter("id", 1L).getSingleResult();
        assertThat(itemUp.getOwner(), equalTo(user));
        assertThat(itemUp.getName(), equalTo(itemDtoUp.getName()));
        assertThat(itemUp.getDescription(), equalTo(itemDtoUp.getDescription()));
        assertThat(itemUp.getItemRequest(), equalTo(request));

        Set<ItemDto> emptyList = itemService.search("", 0, 10);
        assertThat(emptyList.size(), equalTo(0));
    }
}
