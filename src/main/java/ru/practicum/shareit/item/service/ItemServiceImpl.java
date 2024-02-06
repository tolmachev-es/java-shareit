package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.CommentDao;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = {"ru.practicum.shareit.booking", "ru.practicum.shareit.request"})
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;
    private final CommentDao commentDao;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(ItemDto item, long userId) {
        Item newItem = ItemMapper.ITEM_MAPPER.fromDto(item);
        newItem.setOwner(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
        newItem.setItemRequest(item.getRequestId() != null ?
                ItemRequestMapper.ITEM_REQUEST_MAPPER.fromEntity(
                        itemRequestRepository.getById(item.getRequestId())) : null);
        Item createItem = ItemMapper.ITEM_MAPPER.fromEntity(
                itemDao.create(ItemMapper.ITEM_MAPPER.toEntity(newItem)));
        return ItemMapper.ITEM_MAPPER.toDto(createItem);
    }

    @Override
    public ItemDto update(ItemDto item, long userId, long itemId) {
        Item itemToUpdate = ItemMapper.ITEM_MAPPER.fromDto(item);
        itemToUpdate.setOwner(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
        itemToUpdate.setId(itemId);
        Item updateItem = ItemMapper.ITEM_MAPPER.fromEntity(
                itemDao.update(ItemMapper.ITEM_MAPPER.toEntity(itemToUpdate)));
        return ItemMapper.ITEM_MAPPER.toDto(updateItem);
    }

    @Override
    public ItemDto get(Long itemId, Long userId) {
        Item item = ItemMapper.ITEM_MAPPER.fromEntity(itemDao.get(itemId));
        ItemDto itemDto = ItemMapper.ITEM_MAPPER.toDto(item);
        if (item.getOwner().getId().equals(userId)) {
            itemDto = addBooking(itemDto);
            return addComment(itemDto);
        } else {
            return addComment(itemDto);
        }
    }

    @Override
    public Set<ItemDto> getAllByOwner(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        UserEntity user = userDao.getUserById(userId);
        Set<Item> items = itemDao.getByOwner(user, pageable).stream()
                .map(ItemMapper.ITEM_MAPPER::fromEntity)
                .collect(Collectors.toSet());
        return items.stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .map(this::addBooking)
                .map(this::addComment)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ItemDto> search(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return new HashSet<>();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        Set<Item> items = itemDao.search(text, pageable).stream()
                .map(ItemMapper.ITEM_MAPPER::fromEntity)
                .collect(Collectors.toSet());
        return items.stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        Optional<BookingEntity> booking = bookingRepository
                .findTopBookingEntitiesByItem_IdAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now());
        if (booking.isPresent()) {
            Comment newComment = CommentMapper.COMMENT_MAPPER.fromDto(commentDto);
            newComment.setItem(ItemMapper.ITEM_MAPPER.fromEntity(itemDao.get(itemId)));
            newComment.setAuthor(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
            newComment.setCreated(LocalDateTime.now());
            return CommentMapper.COMMENT_MAPPER.toDto(
                    CommentMapper.COMMENT_MAPPER.fromEntity(
                            commentDao.create(CommentMapper.COMMENT_MAPPER.toEntity(newComment))));
        } else {
            throw new InvalidParameterException(String.format("Бронирование вещи с id %s не найдено", itemId));
        }
    }

    private ItemDto addBooking(ItemDto itemDto) {
        Optional<BookingEntity> previousBooking = bookingRepository
                .findTopBookingEntitiesByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
                        itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        Optional<BookingEntity> nextBooking = bookingRepository
                .findTopBookingEntitiesByItem_IdAndStartAfterAndStatusOrderByStartAsc(
                        itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        previousBooking.ifPresent(bookingEntity -> itemDto.setLastBooking(
                BookingMapper.BOOKING_MAPPER.toDtoByItemRequest(
                        BookingMapper.BOOKING_MAPPER.fromEntity(bookingEntity))));
        nextBooking.ifPresent(bookingEntity -> itemDto.setNextBooking(
                BookingMapper.BOOKING_MAPPER.toDtoByItemRequest(
                        BookingMapper.BOOKING_MAPPER.fromEntity(bookingEntity))));
        return itemDto;
    }

    private ItemDto addComment(ItemDto itemDto) {
        Set<Comment> comments = commentDao.getAllByItem(itemDto.getId())
                .stream()
                .map(CommentMapper.COMMENT_MAPPER::fromEntity)
                .collect(Collectors.toSet());
        itemDto.setComments(comments
                .stream()
                .map(CommentMapper.COMMENT_MAPPER::toDto)
                .collect(Collectors.toSet()));
        return itemDto;
    }
}
