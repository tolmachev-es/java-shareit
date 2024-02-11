package ru.practicum.shareit.itemRequest.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    private Long id;
    @NotBlank
    @NotNull
    private String description;
    private LocalDateTime created;
    private Set<ItemDtoForRequest> items;
}
