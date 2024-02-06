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
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.TypedQuery;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringJUnitConfig
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareIt"})
@ComponentScan(basePackages = {"ru.practicum.shareIt"})
class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final TestEntityManager testEntityManager;

    @Test
    @DirtiesContext
    void createNewUser() {
        UserDto userDto = new UserDto();
        userDto.setName("Neo");
        userDto.setEmail("theOne@ihatetest.ru");
        userService.create(userDto);

        TypedQuery<UserEntity> userEntityTypedQuery = testEntityManager
                .getEntityManager()
                .createQuery("select u from UserEntity as u where u.id = :id", UserEntity.class);
        UserEntity entity = userEntityTypedQuery.setParameter("id", 1L).getSingleResult();
        assertThat(entity.getName(), equalTo(userDto.getName()));
        assertThat(entity.getEmail(), equalTo(userDto.getEmail()));

        Set<UserDto> users = userService.getAll();
        assertThat(users.size(), equalTo(1));

        userService.deleteById(1L);
        Set<UserDto> users1 = userService.getAll();
        assertThat(users1.size(), equalTo(0));
    }
}
