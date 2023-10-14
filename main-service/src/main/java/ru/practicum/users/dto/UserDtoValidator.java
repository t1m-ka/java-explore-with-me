package ru.practicum.users.dto;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.util.exception.ValidationException;

import java.util.regex.Pattern;

public class UserDtoValidator {
    public static void validateNewUser(UserDto userDto) {
        final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (StringUtils.isBlank(userDto.getEmail()))
            throw new ValidationException("Incorrectly made request", "The 'Email' field must not be blank");
        if (userDto.getEmail().length() < 6 || userDto.getEmail().length() > 254)
            throw new ValidationException("Incorrectly made request",
                    "The 'Email' field must contain between 6 and 254 chars");
        if (!Pattern.compile(regexPattern).matcher(userDto.getEmail()).matches())
            throw new ValidationException("Incorrectly made request", "The format of the 'Email' field is incorrect");

        if (StringUtils.isBlank(userDto.getName()))
            throw new ValidationException("Incorrectly made request", "The 'name' field must not be blank");
        if (userDto.getName().length() < 2 || userDto.getName().length() > 250)
            throw new ValidationException("Incorrectly made request",
                    "The 'name' field must contain between 2 and 250 chars");
    }
}
