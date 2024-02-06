package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@EntityScan(basePackages = {"ru.practicum.shareIt"})
class DBItemDaoTest {
    private ItemServiceImpl itemService;
    private ItemRepository itemRepository;
    private DBUserDao userDao;
    private ItemEntity item;
    private UserEntity user1;
    private UserEntity user2;
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        itemRepository = Mockito.mock(ItemRepository.class);
        ItemDao itemDto = new DBItemDao(itemRepository);
        bookingRepository = Mockito.mock(BookingRepository.class);
        CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
        ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        CommentDao commentDao = new DBCommentDao(commentRepository);
        userDao = Mockito.mock(DBUserDao.class);
        itemService = new ItemServiceImpl(itemDto,
                userDao, commentDao, bookingRepository, itemRequestRepository);
        user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("egor");
        user1.setEmail("egor@mock.com");

        user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("neegor");
        user2.setEmail("neegor@mock.com");

        item = new ItemEntity();
        item.setId(1L);
        item.setName("knife");
        item.setDescription("rezat'");
        item.setAvailable(false);
        item.setOwner(user1);
    }

    @Test
    void updateItemError() {
        Mockito.when(userDao.getUserById(Mockito.anyLong()))
                .thenReturn(user2);
        Mockito.when(itemRepository.getItemEntityById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("silno rezat'");
        itemDto.setAvailable(true);
        item.setName("sword");

        InvalidParameterException invalidParameterException =
                assertThrows(InvalidParameterException.class,
                        () -> itemService.update(itemDto, 2L, 1L));

        assertThat(invalidParameterException.getMessage(),
                equalTo("Пользователь 2 не не имеет прав для редактирования вещи 1"));
    }

    @Test
    void addComment() {
        Mockito.when(bookingRepository
                        .findTopBookingEntitiesByItem_IdAndBooker_IdAndEndBefore(
                                Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(Optional.empty());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("horosho");
        commentDto.setAuthorName("neo");

        InvalidParameterException invalidParameterException =
                assertThrows(InvalidParameterException.class,
                        () -> itemService.addComment(1L, 1L, commentDto));
        assertThat(invalidParameterException.getMessage(), equalTo("Бронирование вещи с id 1 не найдено"));
    }
}
