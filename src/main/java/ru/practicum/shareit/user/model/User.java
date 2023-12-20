package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.user.model.validationGroups.UserOnCreate;
import ru.practicum.shareit.user.model.validationGroups.UserOnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Long id;
    @NotEmpty(groups = UserOnCreate.class)
    @NotBlank(groups = UserOnCreate.class)
    private String name;
    @NotEmpty(groups = UserOnCreate.class)
    @NotBlank(groups = UserOnCreate.class)
    @Email(groups = {UserOnCreate.class, UserOnUpdate.class})
    private String email;
}
