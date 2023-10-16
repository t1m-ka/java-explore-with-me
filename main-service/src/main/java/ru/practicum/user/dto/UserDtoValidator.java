package ru.practicum.user.dto;

import static ru.practicum.util.VariableValidator.validateStringLength;
import static ru.practicum.util.VariableValidator.validateStringNotBlank;

public class UserDtoValidator {
    public static void validateNewUser(UserDto userDto) {
        final String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        //final String regexPattern = "([A-Za-z0-9]{1,}[\\-]{0,1}[A-Za-z0-9]{1,}[\\.]{0,1}[A-Za-z0-9]{1,})+@"
        //        + "([A-Za-z0-9]{1,}[\\-]{0,1}[A-Za-z0-9]{1,}[\\.]{0,1}[A-Za-z0-9]{1,})+[\\.]{1}[a-z]{2,4}";

        //^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
          //      + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$
        //+ "([A-Za-z0-9]{1,}[\\\\-]{0,1}[A-Za-z0-9]{1,}[\\\\.]{0,1}[A-Za-z0-9]{1,})+[\\\\.]{1}[a-z]{2,4}";
        //final String regexPattern = "^(?=.{1,64}@(.\\\\.[A-Za-z]{2,}){4,100})[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$";

        final int MIN_EMAIL_LENGTH = 6;
        final int MAX_EMAIL_LENGTH = 254;
        final int MIN_NAME_LENGTH = 2;
        final int MAX_NAME_LENGTH = 250;

        validateStringNotBlank(userDto.getEmail(), "email");
        validateStringLength(userDto.getEmail(),  "email",  MIN_EMAIL_LENGTH,  MAX_EMAIL_LENGTH);
        /*if (!Pattern.compile(regexPattern).matcher(userDto.getEmail()).matches())
            throw new ValidationException("Incorrectly made request",
                    "Field: email. Error: некорректный формат адреса электронной почты. Value: " + userDto.getEmail());*/

        validateStringNotBlank(userDto.getName(), "name");
        validateStringLength(userDto.getName(),  "name",  MIN_NAME_LENGTH,  MAX_NAME_LENGTH);
    }
}
