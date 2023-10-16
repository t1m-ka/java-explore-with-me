package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u "
            + "FROM User as u "
            + "WHERE u.id IN ?1")
    List<User> findAllByIdList(List<Long> ids, Pageable page);
}
