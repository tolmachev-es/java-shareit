package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.CommentEntity;
import ru.practicum.shareit.item.dao.DBCommentDao;
import ru.practicum.shareit.item.dao.DBItemDao;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringJUnitConfig({ItemServiceImpl.class, DBItemDao.class, DBUserDao.class, DBCommentDao.class})
@DataJpaTest(showSql = false)
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class ItemServiceImplTest {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private DBUserDao userDao;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private DBItemDao itemDao;


    @BeforeEach
    @DirtiesContext
    void createUser() {
        UserEntity user1 = new UserEntity();
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");
        userDao.create(user1);
        UserEntity user2 = new UserEntity();
        user2.setName("Trinity");
        user2.setEmail("ilovetheone@matrix.com");
        userDao.create(user2);
    }

    @Test
    @DirtiesContext
    void createItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Red Pill");
        itemDto.setDescription("eat and wake up");
        itemDto.setAvailable(true);
        itemService.create(itemDto, 1L);
        TypedQuery<ItemEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select i from ItemEntity as i where id = :id", ItemEntity.class);
        ItemEntity entity = query.setParameter("id", 1L).getSingleResult();
        assertThat(entity.getId(), notNullValue());
        assertThat(entity.getName(), equalTo(itemDto.getName()));
        assertThat(entity.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(entity.getOwner().getId(), equalTo(1L));
    }

    @Test
    @DirtiesContext
    void getAllItem() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Red Pill");
        itemDto1.setDescription("eat and wake up");
        itemDto1.setAvailable(true);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Blue Pill");
        itemDto2.setDescription("eat and sleep");
        itemDto2.setAvailable(true);
        itemService.create(itemDto1, 1L);
        itemService.create(itemDto2, 2L);

        List<ItemDto> all = new ArrayList<>(itemService.getAllByOwner(1L, 0, 10));
        assertThat(all.size(), equalTo(1));
        assertThat(all.get(0).getId(), equalTo(1L));
    }

    @Test
    @DirtiesContext
    void addComment() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Red Pill");
        itemDto1.setDescription("eat and wake up");
        itemDto1.setAvailable(true);
        itemService.create(itemDto1, 1L);

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setItem(itemDao.get(1L));
        bookingEntity.setStatus(BookingStatus.APPROVED);
        bookingEntity.setStart(LocalDateTime.MIN);
        bookingEntity.setEnd(LocalDateTime.now());
        bookingEntity.setBooker(userDao.getUserById(2L));
        bookingRepository.save(bookingEntity);

        CommentDto commentDto = new CommentDto();
        commentDto.setAuthorName("Neo");
        commentDto.setText("now I was being followed by an agent");
        itemService.addComment(2L, 1L, commentDto);

        TypedQuery<CommentEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select c from CommentEntity as c where id = :id", CommentEntity.class);
        CommentEntity entity = query.setParameter("id", 1L).getSingleResult();

        assertThat(entity.getItem().getId(), equalTo(1L));
        assertThat(entity.getId(), equalTo(1L));
        assertThat(entity.getText(), equalTo(commentDto.getText()));
        assertThat(entity.getAuthor().getId(), equalTo(2L));
    }

    @Test
    @DirtiesContext
    void getById() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Red Pill");
        itemDto1.setDescription("eat and wake up");
        itemDto1.setAvailable(true);
        itemService.create(itemDto1, 1L);

        BookingEntity bookingEntity2 = new BookingEntity();
        bookingEntity2.setItem(itemDao.get(1L));
        bookingEntity2.setStatus(BookingStatus.APPROVED);
        bookingEntity2.setStart(LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.now()));
        bookingEntity2.setEnd(LocalDateTime.MAX);
        bookingEntity2.setBooker(userDao.getUserById(2L));
        bookingRepository.save(bookingEntity2);

        ItemDto itemDtoUpdate = new ItemDto();
        itemDtoUpdate.setName("Red Pill1");
        itemDtoUpdate.setDescription("eat and wake up1");
        itemDtoUpdate.setAvailable(true);


        ItemDto itemDto2 = itemService.get(1L, 1L);
        assertThat(itemDto2.getLastBooking(), equalTo(null));
        assertThat(itemDto2.getName(), equalTo(itemDto1.getName()));
        assertThat(itemDto2.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(itemDto2.getNextBooking().getId(), equalTo(1L));


        itemService.update(itemDtoUpdate, 1L, 1L);
        ItemDto itemDto3 = itemService.get(1L, 1L);
        assertThat(itemDto3.getLastBooking(), equalTo(null));
        assertThat(itemDto3.getName(), equalTo(itemDtoUpdate.getName()));
        assertThat(itemDto3.getDescription(), equalTo(itemDtoUpdate.getDescription()));
        assertThat(itemDto3.getNextBooking().getId(), equalTo(1L));
    }


    @Test
    @DirtiesContext
    void search() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Red Pill");
        itemDto1.setDescription("eat and wake up");
        itemDto1.setAvailable(true);
        itemService.create(itemDto1, 1L);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Blue Pill");
        itemDto2.setDescription("NOT RED pill");
        itemDto2.setAvailable(true);
        itemService.create(itemDto1, 2L);


        Set<ItemDto> search = itemService.search("red", 0, 10);
        assertThat(search.size(), equalTo(2));
    }

    @Test
    @DirtiesContext
    void updateWithout() {
        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Red Pill");
        itemDto1.setDescription("eat and wake up");
        itemDto1.setAvailable(true);
        itemService.create(itemDto1, 1L);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setAvailable(false);
        itemService.update(itemDto2, 1L, 1L);

        ItemDto getItem = itemService.get(1L, 1L);
        assertThat(getItem.getName(), equalTo(itemDto1.getName()));
        assertThat(getItem.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(getItem.getAvailable(), equalTo(false));
    }
}
