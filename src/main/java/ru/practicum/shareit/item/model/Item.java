package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.item.model.validationGroups.ItemOnCreate;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@ToString
public class Item {
    private Long id;
    @NotBlank(groups = ItemOnCreate.class)
    @NotEmpty(groups = ItemOnCreate.class)
    private String name;
    @NotBlank(groups = ItemOnCreate.class)
    @NotEmpty(groups = ItemOnCreate.class)
    private String description;
    @NotNull(groups = ItemOnCreate.class)
    private Boolean available;
    private User owner;
    private ItemRequest itemRequest;
}
