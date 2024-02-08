package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.item.dao.item.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringJUnitConfig
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@ComponentScan(basePackages = {"ru.practicum.shareit"})
class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final TestEntityManager testEntityManager;

    @Test
    @DirtiesContext
    void getAllByUserId() {
        UserEntity user1 = new UserEntity();
        user1.setName("neo");
        user1.setEmail("theone@mock.com");
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("trinity");
        user2.setEmail("iloveneo@mock.com");
        userRepository.save(user2);

        ItemRequestEntity itemRequest1 = new ItemRequestEntity();
        itemRequest1.setRequestor(user1);
        itemRequest1.setDescription("i want red pill");
        itemRequest1.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest1);

        ItemEntity item = new ItemEntity();
        item.setOwner(user2);
        item.setItemRequest(itemRequest1);
        item.setAvailable(true);
        item.setName("red pill");
        item.setDescription("take and wake up");
        itemRepository.save(item);

        ItemRequestEntity itemRequest2 = new ItemRequestEntity();
        itemRequest2.setRequestor(user1);
        itemRequest2.setDescription("i need good glass");
        itemRequest2.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest2);

        ItemRequestEntity itemRequest3 = new ItemRequestEntity();
        itemRequest3.setRequestor(user1);
        itemRequest3.setDescription("i need guns");
        itemRequest3.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest3);

        ItemEntity item2 = new ItemEntity();
        item2.setOwner(user2);
        item2.setItemRequest(itemRequest2);
        item2.setAvailable(true);
        item2.setName("black glass");
        item2.setDescription("it fit on you");
        itemRepository.save(item2);

        ItemEntity item3 = new ItemEntity();
        item3.setOwner(user2);
        item3.setItemRequest(itemRequest2);
        item3.setAvailable(true);
        item3.setName("black glass");
        item3.setDescription("it fit on you");
        itemRepository.save(item3);

        List<ItemRequestDto> getAllByUserId = new ArrayList<>(itemRequestService.get(1L));
        assertThat(getAllByUserId.size(), equalTo(3));
    }

    @Test
    @DirtiesContext
    void createNewRequest() {
        UserEntity user1 = new UserEntity();
        user1.setName("neo");
        user1.setEmail("theone@mock.com");
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("trinity");
        user2.setEmail("iloveneo@mock.com");
        userRepository.save(user2);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("i want red pill");

        ItemRequestDto createdRequest = itemRequestService.create(itemRequestDto, 1L);
        TypedQuery<ItemRequestEntity> query = testEntityManager
                .getEntityManager()
                .createQuery("select ir from ItemRequestEntity as ir where id = :id", ItemRequestEntity.class);

        ItemRequestEntity itemRequest = query.setParameter("id", 1L).getSingleResult();
        assertThat(itemRequest.getCreated(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getRequestor().getId(), equalTo(1L));
    }
}