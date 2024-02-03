package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SpringJUnitConfig
class DBItemDaoTest {
    private static DBItemDao dao;
    private static ItemRepository itemRepository;

    @BeforeAll
    static void init() {
        itemRepository = Mockito.mock(ItemRepository.class);
        dao = new DBItemDao(itemRepository);
    }

    @Test
    void getEmptyItem() {
        Optional<ItemEntity> item = Optional.empty();
        Mockito.when(itemRepository.getItemEntityById(1L))
                .thenReturn(item);
        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> dao.get(1L));
        assertThat(objectNotFound.getMessage(),
                equalTo(String.format("Вещь с id %s не найдена", 1L)));
    }

    @Test
    void getItem() {
        ItemEntity item = new ItemEntity();
        item.setId(1L);
        item.setName("Red pill");
        item.setDescription("take and wake up");
        item.setAvailable(true);
        Optional<ItemEntity> itemGet = Optional.of(item);
        Mockito.when(itemRepository.getItemEntityById(1L))
                .thenReturn(itemGet);
        ItemEntity getItem = dao.get(1L);
        assertThat(getItem.getId(), equalTo(1L));
        assertThat(getItem.getName(), equalTo("Red pill"));
    }

    @Test
    void updateWithException() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("Neo");
        user1.setEmail("theone@matrix.com");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("Trinity");
        user2.setEmail("ilovetheone@matrix.com");

        ItemEntity item1 = new ItemEntity();
        item1.setId(1L);
        item1.setName("Red pill");
        item1.setDescription("take and wake up");
        item1.setAvailable(true);
        item1.setOwner(user1);

        ItemEntity item2 = new ItemEntity();
        item2.setId(1L);
        item2.setName("Red pill");
        item2.setDescription("take and wake up");
        item2.setAvailable(true);
        item2.setOwner(user2);


        Optional<ItemEntity> itemGet = Optional.of(item1);
        Mockito.when(itemRepository.getItemEntityById(1L)).thenReturn(itemGet);

        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> dao.update(item2));
        assertThat(invalidParameterException.getMessage(),
                equalTo(String.format("Пользователь %s не не имеет прав для редактирования вещи %s",
                        user2.getId(),
                        item1.getId())));
    }
}
