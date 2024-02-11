package ru.practicum.shareit.booking.dao;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "PUBLIC", name = "BOOKING")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime start;
    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private ItemEntity item;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity booker;
    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
