package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.validationGroups.ItemOnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    @NotBlank(groups = ItemOnCreate.class)
    @NotEmpty(groups = ItemOnCreate.class)
    private String name;
    @NotBlank(groups = ItemOnCreate.class)
    @NotEmpty(groups = ItemOnCreate.class)
    private String description;
    @NotNull(groups = ItemOnCreate.class)
    private Boolean available;
}
