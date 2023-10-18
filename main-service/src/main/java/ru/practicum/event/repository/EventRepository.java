package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    @Query("select event "
            + "from Event as event "
            + "join event.initiator as initiator "
            + "join event.category as category "
            + "where (:userIds is null OR initiator.id IN :userIds) "
            + "and (:states is null OR event.state IN :states) "
            + "and (:categoryIds is null OR category.id IN :categoryIds) "
            + "and (cast(:rangeStart as timestamp) is null OR event.eventDate > :rangeStart) "
            + "and (cast(:rangeEnd as timestamp) is null OR event.eventDate < :rangeEnd)")
    List<Event> searchEventsByAdmin(
            @Param("userIds") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categoryIds") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("select event "
            + "from Event as event "
            + "join event.category as category "
            + "where (lower(event.annotation) like lower(concat('%', :text, '%')) "
            + "or lower(event.description) like lower(concat('%', :text, '%'))) "
            + "and (:paid is null OR event.paid = :paid) "
            + "and (:onlyAvailable = true OR (event.confirmedRequests < event.participantLimit)) "
            + "and (:categoryIds is null OR category.id IN :categoryIds) "
            + "and (cast(:rangeStart as timestamp) is null OR event.eventDate > :rangeStart) "
            + "and (cast(:rangeEnd as timestamp) is null OR event.eventDate < :rangeEnd)")
    List<Event> getEventFiltered(
            @Param("text") String text,
            @Param("paid") Boolean paid,
            @Param("onlyAvailable") boolean onlyAvailable,
            @Param("categoryIds") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    @Query("select event "
            + "from Event as event "
            + "where event.id IN ?1 "
            + "and event.state = 'PUBLISHED'")
    List<Event> findAllPublishedByEventIdList(List<Long> events);

    @Query("select event "
            + "from Event as event "
            + "where event.id = ?1 "
            + "and event.state = 'PUBLISHED'")
    Optional<Event> findPublishedEvent(long eventId);
}
