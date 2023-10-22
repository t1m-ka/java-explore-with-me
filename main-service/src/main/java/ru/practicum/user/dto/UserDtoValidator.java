package ru.practicum.user.dto;

import ru.practicum.util.exception.ValidationException;

import java.util.regex.Pattern;

import static ru.practicum.util.VariableValidator.validateStringLength;
import static ru.practicum.util.VariableValidator.validateStringNotBlank;

public class UserDtoValidator {
    public static void validateNewUser(UserDto userDto) {
        final String regexPattern = "^[\\p{L}0-9!#$%&'*+\\/=?^_`{|}~-][\\p{L}0-9.!#$%&'*+\\/=?^_`{|}~-]{0,63}@"
                + "[\\p{L}0-9-]{1,63}+(?:\\.[\\p{L}0-9-]{2,})*$";

        final int MIN_EMAIL_LENGTH = 6;
        final int MAX_EMAIL_LENGTH = 254;
        final int MIN_NAME_LENGTH = 2;
        final int MAX_NAME_LENGTH = 250;

        validateStringNotBlank(userDto.getEmail(), "email");
        validateStringLength(userDto.getEmail(), "email", MIN_EMAIL_LENGTH, MAX_EMAIL_LENGTH);
        if (!Pattern.compile(regexPattern).matcher(userDto.getEmail()).matches())
            throw new ValidationException("Incorrectly made request",
                    "Field: email. Error: некорректный формат адреса электронной почты. Value: " + userDto.getEmail());

        validateStringNotBlank(userDto.getName(), "name");
        validateStringLength(userDto.getName(), "name", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
    }
}
