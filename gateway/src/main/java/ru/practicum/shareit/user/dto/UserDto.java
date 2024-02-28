package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.validationGroups.UserOnCreate;
import ru.practicum.shareit.user.dto.validationGroups.UserOnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(groups = UserOnCreate.class)
    @NotBlank(groups = UserOnCreate.class)
    private String name;
    @NotEmpty(groups = UserOnCreate.class)
    @NotBlank(groups = UserOnCreate.class)
    @Email(groups = {UserOnCreate.class, UserOnUpdate.class})
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
