package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.util.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.users.dto.UserMapper.toUser;
import static ru.practicum.users.dto.UserMapper.toUserDto;
import static ru.practicum.util.PageParamsMaker.makePageable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<User> userList = repository.findAllByIdList(ids, makePageable(from, size));
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return toUserDto(repository.save(toUser(userDto)));
    }

    @Override
    public void deleteUserById(long userId) {
        repository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "User with id=" + userId + "was not found"));
        repository.deleteById(userId);
    }
}
