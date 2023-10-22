package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select com "
            + "from Comment as com "
            + "join com.event as event "
            + "where event.id = :eventId "
            + "and (cast(:rangeStart as timestamp) is null OR event.eventDate > :rangeStart) "
            + "and (cast(:rangeEnd as timestamp) is null OR event.eventDate < :rangeEnd) "
            + "order by com.createdOn DESC")
    List<Comment> findEventCommentsByFilter(
            @Param("eventId") Long eventId,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("select com "
            + "from Comment as com "
            + "join com.author as author "
            + "join com.event as event "
            + "where author.id = :userId "
            + "and (:eventId is null or event.id = :eventId) "
            + "and (cast(:rangeStart as timestamp) is null OR event.eventDate > :rangeStart) "
            + "and (cast(:rangeEnd as timestamp) is null OR event.eventDate < :rangeEnd) "
            + "order by com.createdOn DESC")
    List<Comment> findUserCommentsByFilter(
            @Param("userId") Long userId,
            @Param("eventId") Long eventId,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);


}
