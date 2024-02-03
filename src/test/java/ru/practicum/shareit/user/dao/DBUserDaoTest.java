package ru.practicum.shareit.user.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.HeadExeptions.ConflictException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.item.dao.ItemRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SpringJUnitConfig({ItemRepository.class, ObjectNotFound.class, String.class})
class DBUserDaoTest {

    private static DBUserDao dbUserDao;
    private static UserRepository userRepository;

    @BeforeAll
    static void init() {
        userRepository = Mockito.mock(UserRepository.class);
        dbUserDao = new DBUserDao(userRepository);
    }

    @Test
    void createWithConflictEmail() {
        String email = "user@user.ru";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName("name");
        Mockito.when(userRepository.save(Mockito.any()))
                .thenThrow(new DataIntegrityViolationException("error"));
        ConflictException conflictException = assertThrows(ConflictException.class,
                () -> dbUserDao.create(user));
        assertThat(conflictException.getMessage(), equalTo(String.format("Пользователь с Email %s уже есть в системе",
                user.getEmail())));
    }

    @Test
    void getEmptyUser() {
        Long id = 1L;
        Optional<UserEntity> user = Optional.empty();
        Mockito.when(userRepository.getUserById(Mockito.any()))
                .thenReturn(user);
        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> dbUserDao.getUserById(id));
        assertThat(objectNotFound.getMessage(), equalTo(String.format("Пользователь с id %s не найден", id)));
    }
}
