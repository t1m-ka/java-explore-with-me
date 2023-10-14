package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import java.util.List;

import static ru.practicum.users.dto.UserDtoValidator.validateNewUser;
import static ru.practicum.util.VariableValidator.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        ids.forEach(x -> validateNotNullVariable(x, "id"));
        ids.forEach(x -> validatePositiveVariable(x, "id"));
        validatePageableParams(from, size);
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        validateNewUser(userDto);
        return service.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        validateNotNullVariable(userId, "userId");
        validatePositiveVariable(userId, "userId");
        service.deleteUserById(userId);
    }
}
