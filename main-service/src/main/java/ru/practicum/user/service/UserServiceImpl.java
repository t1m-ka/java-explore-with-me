package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.user.dto.UserMapper.toUser;
import static ru.practicum.user.dto.UserMapper.toUserDto;
import static ru.practicum.util.PageParamsMaker.makePageable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<User> userList;
        if (ids == null)
            userList = repository.findAll(makePageable(from, size)).toList();
        else
            userList = repository.findAllByIdList(ids, makePageable(from, size));
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
