package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.validationGroups.ItemOnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return Objects.equals(id, itemDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
