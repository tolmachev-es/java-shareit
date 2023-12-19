package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.item.model.validationGroups.OnCreate;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    @NotBlank(groups = OnCreate.class)
    @NotEmpty(groups = OnCreate.class)
    private String name;
    @NotBlank(groups = OnCreate.class)
    @NotEmpty(groups = OnCreate.class)
    private String description;
    @NotNull(groups = OnCreate.class)
    private Boolean available;
    private User owner;
    private ItemRequest itemRequest;
}
