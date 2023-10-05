package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Slf4j
@Validated
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) int from,
            @RequestParam(required = false) int size) {
        return null;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return null;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {

    }
}
