package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@AllArgsConstructor
public class UserDto {
    long id;

    @NotBlank
    String name;

    @Email
    String email;
}
