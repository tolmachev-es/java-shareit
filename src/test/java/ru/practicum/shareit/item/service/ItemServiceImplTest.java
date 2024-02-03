package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dao.DBItemDao;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringJUnitConfig({ItemServiceImpl.class, DBItemDao.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class ItemServiceImplTest {

    private final ItemServiceImpl itemService;
    private final DBUserDao userDao;
    private final TestEntityManager testEntityManager;

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

}
