package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.user.dto.validationGroups.UserOnCreate;
import ru.practicum.shareit.user.dto.validationGroups.UserOnUpdate;

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
