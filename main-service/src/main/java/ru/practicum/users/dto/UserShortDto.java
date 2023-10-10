package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@AllArgsConstructor
public class UserShortDto {
    long id;

    @NotBlank
    String name;
}
