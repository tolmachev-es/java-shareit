package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dao.DBItemDao;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringJUnitConfig({DBUserDao.class, DBItemDao.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final UserDao userDao;
    private final TestEntityManager testEntityManager;
    private final ItemDao itemDao;
    private final JdbcTemplate jdbcTemplate;

    @Test
    @DirtiesContext
    void create() {
        UserEntity user1 = new UserEntity();
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");
        user1 = userDao.create(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("Trinity");
        user2.setEmail("ilovetheone@matrix.com");
        userDao.create(user2);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("Red Pill");
        itemEntity.setDescription("eat and wake up");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(user1);
        itemDao.create(itemEntity);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setCreated(LocalDateTime.MIN);
        itemRequestDto.setDescription("Ищу отвертку");
        itemRequestService.create(itemRequestDto, 1L);

        TypedQuery<ItemRequestEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select i from ItemRequestEntity as i where id = :id", ItemRequestEntity.class);
        ItemRequestEntity entity = query.setParameter("id", 1L).getSingleResult();
        assertThat(entity.getCreated(), notNullValue());
        assertThat(entity.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(entity.getRequestor().getId(), equalTo(1L));
    }


    @Test
    @DirtiesContext
    void getItemRequest() {
        UserEntity user1 = new UserEntity();
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");
        user1 = userDao.create(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("Trinity");
        user2.setEmail("ilovetheone@matrix.com");
        userDao.create(user2);

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("Red Pill");
        itemEntity.setDescription("eat and wake up");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(user1);
        itemDao.create(itemEntity);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setCreated(LocalDateTime.MIN);
        itemRequestDto.setDescription("Ищу отвертку");
        itemRequestService.create(itemRequestDto, 1L);

        ItemRequestDto itemRequestDto1 = itemRequestService.getById(1L, 1L);

        assertThat(itemRequestDto1.getCreated(), notNullValue());
        assertThat(itemRequestDto1.getDescription(), equalTo("Ищу отвертку"));

        Set<ItemRequestDto> requestDtos = itemRequestService.getWithPagination(2L, 0, 10);
        assertThat(requestDtos.size(), equalTo(1));
    }

    @Test
    @DirtiesContext
    void getByUserId() {
        UserEntity user1 = new UserEntity();
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");
        userDao.create(user1);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setCreated(LocalDateTime.MIN);
        itemRequestDto.setDescription("Ищу отвертку");
        itemRequestService.create(itemRequestDto, 1L);

        TypedQuery<ItemRequestEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select i from ItemRequestEntity as i where id = :id", ItemRequestEntity.class);
        ItemRequestEntity entity = query.setParameter("id", 1L).getSingleResult();

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("Red Pill");
        itemEntity.setDescription("eat and wake up");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(user1);
        itemEntity.setItemRequest(entity);
        itemDao.create(itemEntity);

        Set<ItemRequestDto> itemRequestDto1 = itemRequestService.get(1L);
        assertThat(itemRequestDto1.size(), equalTo(1));
        assertThat(new ArrayList<>(itemRequestDto1).get(0).getItems().size(), equalTo(1));
        assertThat(new ArrayList<>(new ArrayList<>(itemRequestDto1).get(0).getItems()).get(0).getId(),
                equalTo(1L));
    }

    @Test
    @DirtiesContext
    void getByUserIdAndRequestId() {
        UserEntity user1 = new UserEntity();
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");
        userDao.create(user1);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setCreated(LocalDateTime.MIN);
        itemRequestDto.setDescription("Ищу отвертку");
        itemRequestService.create(itemRequestDto, 1L);

        TypedQuery<ItemRequestEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select i from ItemRequestEntity as i where id = :id", ItemRequestEntity.class);
        ItemRequestEntity entity = query.setParameter("id", 1L).getSingleResult();

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("Red Pill");
        itemEntity.setDescription("eat and wake up");
        itemEntity.setAvailable(true);
        itemEntity.setOwner(user1);
        itemEntity.setItemRequest(entity);
        itemDao.create(itemEntity);

        ItemRequestDto itemRequestDto1 = itemRequestService.getById(1L, 1L);
        assertThat(itemRequestDto1.getItems().size(), equalTo(1));
    }
}
