package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.item.dao.item.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
class ItemRequestRepositoryTest {
    private ItemRequestServiceImpl itemRequestService;
    private UserDao userDao;
    private ItemRequestRepository itemRequestRepository;
    private ItemRepository itemRepository;
    private UserEntity user1;
    private UserEntity user2;
    private ItemRequestEntity itemRequest1;
    private ItemEntity item;


    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(DBUserDao.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(userDao, itemRequestRepository, itemRepository);

        user1 = new UserEntity();
        user1.setName("neo");
        user1.setEmail("theone@mock.com");

        itemRequest1 = new ItemRequestEntity();
        itemRequest1.setRequestor(user1);
        itemRequest1.setDescription("i want red pill");
        itemRequest1.setCreated(LocalDateTime.now());

        user2 = new UserEntity();
        user2.setName("trinity");
        user2.setEmail("iloveneo@mock.com");

        item = new ItemEntity();
        item.setOwner(user2);
        item.setItemRequest(itemRequest1);
        item.setAvailable(true);
        item.setName("red pill");
        item.setDescription("take and wake up");
    }

    @Test
    void getWithPagination() {
        ItemRequestEntity itemRequest2 = new ItemRequestEntity();
        itemRequest2.setRequestor(user1);
        itemRequest2.setDescription("i need good glass");
        itemRequest2.setCreated(LocalDateTime.now());

        List<ItemRequestEntity> allRequest = List.of(itemRequest1, itemRequest2);

        Mockito.when(itemRepository.getItemEntitiesByItemRequestNotNull())
                .thenReturn(Set.of(item));
        Page<ItemRequestEntity> page = new PageImpl<>(allRequest);
        Mockito.when(itemRequestRepository.findAllByRequestor_IdNotOrderByCreated(
                        Mockito.anyLong(), Mockito.any()))
                .thenReturn(page);

        Set<ItemRequestDto> allItem = itemRequestService.getWithPagination(1L, 0, 10);
        assertThat(allItem.size(), equalTo(2));
    }

    @Test
    void getWithWrongPagination() {
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class,
                () -> itemRequestService.getWithPagination(1L, -1, -1));

        assertThat(invalidParameterException.getMessage(), equalTo("Неверно указаны параметры отображения"));
    }

    @Test
    void getById() {
        Mockito.when(userDao.getUserById(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest1));
        Mockito.when(itemRepository.getItemEntitiesByItemRequest_Id(Mockito.anyLong()))
                .thenReturn(Set.of(item));

        ItemRequestDto get = itemRequestService.getById(1L, 1L);
        assertThat(get.getItems().size(), equalTo(1));
        assertThat(get.getDescription(), equalTo(itemRequest1.getDescription()));
    }

    @Test
    void getByIdRequestNotFound() {
        Mockito.when(userDao.getUserById(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        ObjectNotFound objectNotFound = assertThrows(ObjectNotFound.class,
                () -> itemRequestService.getById(1L, 1L));
        assertThat(objectNotFound.getMessage(), equalTo("Запрос с id 1 не найден"));
    }

    @Test
    void getByIdItemEmpty() {
        Mockito.when(userDao.getUserById(Mockito.anyLong()))
                .thenReturn(user1);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest1));
        Mockito.when(itemRepository.getItemEntitiesByItemRequest_Id(Mockito.anyLong()))
                .thenReturn(new HashSet<>());

        ItemRequestDto get = itemRequestService.getById(1L, 1L);
        assertThat(get.getItems().size(), equalTo(0));
    }
}