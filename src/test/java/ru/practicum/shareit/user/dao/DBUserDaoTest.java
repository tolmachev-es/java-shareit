package ru.practicum.shareit.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.HeadExeptions.ConflictException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@SpringJUnitConfig
class DBUserDaoTest {
    private UserServiceImpl userService;
    private UserDao userDao;
    private UserRepository userRepository;
    private UserDto user1;
    private UserEntity userEn1;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userDao = new DBUserDao(userRepository);
        userService = new UserServiceImpl(userDao);
        userEn1 = new UserEntity();
        userEn1.setId(1L);
        userEn1.setName("neo1");
        userEn1.setEmail("email1@mock.ru");
        user1 = new UserDto();
        user1.setName("neo");
        user1.setEmail("email@mock.ru");
    }

    @Test
    void getById() {
        Mockito.when(userRepository.getUserById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(userEn1));

        UserDto userGet = userService.getById(1L);
        assertThat(userGet.getName(), equalTo(userEn1.getName()));
        assertThat(userGet.getEmail(), equalTo(userGet.getEmail()));
    }

    @Test
    void createWithException() {
        Mockito.when(userRepository.save(Mockito.any()))
                .thenThrow(DataIntegrityViolationException.class);

        ConflictException conflictException = assertThrows(ConflictException.class,
                () -> userService.create(user1));

        assertThat(conflictException.getMessage(), equalTo(String.format("Пользователь с Email %s уже есть в системе",
                user1.getEmail())));
    }

    @Test
    void updateWithoutException() {
        Mockito.when(userRepository.getUserById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(userEn1));

        UserDto userDto = userService.update(user1, 1L);
        assertThat(userDto.getName(), equalTo(user1.getName()));
        assertThat(userDto.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    void updateWithoutExceptionEmpty() {
        Mockito.when(userRepository.getUserById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(userEn1));
        UserDto userDtoBefore = new UserDto();

        UserDto userDto = userService.update(userDtoBefore, 1L);
        assertThat(userDto.getName(), equalTo(userEn1.getName()));
        assertThat(userDto.getEmail(), equalTo(userEn1.getEmail()));
    }

    @Test
    void updateWithException() {
        Mockito.when(userRepository.getUserById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(userEn1));
        Mockito.when(userRepository.save(Mockito.any()))
                .thenThrow(DataIntegrityViolationException.class);

        ConflictException conflictException = assertThrows(ConflictException.class,
                () -> userService.update(user1, 1L));

        assertThat(conflictException.getMessage(), equalTo(
                String.format("Пользователь с Email %s уже есть в системе",
                        user1.getEmail())));
    }

    @Test
    void userGetNotFound() {
        Mockito.when(userRepository.getUserById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> userService.getById(1L));

        assertThat(objectNotFound.getMessage(), equalTo(String.format("Пользователь с id %s не найден", 1L)));
    }

}
