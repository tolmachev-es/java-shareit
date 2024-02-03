package ru.practicum.shareit.user.service;

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
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringJUnitConfig({UserServiceImpl.class, DBUserDao.class, UserEntity.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class UserServiceImplTest {

    private final UserServiceImpl userService;

    private final TestEntityManager testEntityManager;

    @Test
    @DirtiesContext
    void createNewUser() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail("email1@yandex.ru");
        userService.create(user);
        TypedQuery<UserEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select u from UserEntity as u where u.id = :id", UserEntity.class);
        UserEntity entity = query.setParameter("id", 1L).getSingleResult();
        assertThat(entity.getId(), notNullValue());
        assertThat(entity.getName(), equalTo(user.getName()));
        assertThat(entity.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DirtiesContext
    void updateUser() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail("email1@yandex.ru");
        userService.create(user);
        user.setName("newname");
        user.setEmail("newEmail@yandex.ru");
        userService.update(user, 1L);
        TypedQuery<UserEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select u from UserEntity as u", UserEntity.class);
        List<UserEntity> userEntities = query.getResultList();
        assertThat(userEntities.size(), equalTo(1));
        assertThat(userEntities.get(0).getId(), equalTo(1L));
        assertThat(userEntities.get(0).getName(), equalTo(user.getName()));
        assertThat(userEntities.get(0).getEmail(), equalTo(user.getEmail()));
    }

    @Test
    @DirtiesContext
    void deleteUser() {
        UserDto user = new UserDto();
        user.setName("name");
        user.setEmail("email1@yandex.ru");
        userService.create(user);
        TypedQuery<UserEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select u from UserEntity as u", UserEntity.class);
        List<UserEntity> userEntities = query.getResultList();
        assertThat(userEntities.size(), equalTo(1));

        userService.deleteById(1L);
        List<UserEntity> userEntities2 = query.getResultList();
        assertThat(userEntities2.size(), equalTo(0));
    }

    @Test
    @DirtiesContext
    void getAll() {
        UserDto user1 = new UserDto();
        user1.setName("name1");
        user1.setEmail("email1@yandex.ru");
        userService.create(user1);
        UserDto user2 = new UserDto();
        user2.setName("name2");
        user2.setEmail("email2@yandex.ru");
        userService.create(user2);
        UserDto user3 = new UserDto();
        user3.setName("name3");
        user3.setEmail("email3@yandex.ru");
        userService.create(user3);

        Set<UserDto> users = userService.getAll();
        assertThat(users.size(), equalTo(3));
    }

}
