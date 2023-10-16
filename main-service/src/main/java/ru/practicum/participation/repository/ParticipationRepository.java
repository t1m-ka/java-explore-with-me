package ru.practicum.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.participation.dto.ParticipationRequestStatus;
import ru.practicum.participation.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "where event.id = ?1 "
            + "and participation.requestStatus = ?2")
    List<ParticipationRequest> findAllPublishedEventsById(long eventId, ParticipationRequestStatus status);
}
