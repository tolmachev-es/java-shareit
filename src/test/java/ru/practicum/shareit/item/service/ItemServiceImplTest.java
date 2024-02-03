package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Template;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.dao.CommentDao;
import ru.practicum.shareit.item.dao.DBCommentDao;
import ru.practicum.shareit.item.dao.DBItemDao;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.DBUserDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.TypedQuery;
import javax.sql.DataSource;

@Transactional
@SpringJUnitConfig({ItemServiceImpl.class, DBItemDao.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@DataJpaTest
@EntityScan(basePackages = {"ru.practicum.shareit"})
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class ItemServiceImplTest {

    @Mock
    private final DBUserDao userDao;
    @Mock
    private final DBCommentDao commentDao;
    private final DBItemDao itemDao;
    @Mock
    private final BookingRepository bookingRepository;
    @Mock
    private final ItemRequestRepository itemRequestRepository;
    private DataSource dataSource;

    @Test
    void createItem() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Neo");
        user.setEmail("theone@matrix.com");
        ItemServiceImpl itemService = new ItemServiceImpl(itemDao,
                userDao,
                commentDao,
                bookingRepository,
                itemRequestRepository);
        Mockito.when(userDao.getUserById(Mockito.anyLong())).thenReturn(user);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Red Pill");
        itemDto.setDescription("eat and wake up");
        itemDto.setAvailable(true);
        itemService.create(itemDto, 1L);
    }

}
