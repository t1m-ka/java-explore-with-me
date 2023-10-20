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

import static ru.practicum.user.dto.UserDtoValidator.validateNewUser;
import static ru.practicum.user.dto.UserMapper.toUser;
import static ru.practicum.user.dto.UserMapper.toUserDto;
import static ru.practicum.util.PageParamsMaker.makePageable;
import static ru.practicum.util.VariableValidator.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (ids != null) {
            ids.forEach(x -> validateNotNullObject(x, "id"));
            ids.forEach(x -> validatePositiveNumber(x, "id"));
        }
        validatePageableParams(from, size);

        List<User> userList;
        if (ids == null)
            userList = repository.findAll(makePageable(from, size)).toList();
        else
            userList = repository.findAllByIdList(ids, makePageable(from, size));
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validateNewUser(userDto);
        return toUserDto(repository.save(toUser(userDto)));
    }

    @Override
    public void deleteUserById(Long userId) {
        validateNotNullObject(userId, "userId");
        validatePositiveNumber(userId, "userId");
        repository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Required entity not found",
                        "User with id=" + userId + "was not found"));
        repository.deleteById(userId);
    }
}
