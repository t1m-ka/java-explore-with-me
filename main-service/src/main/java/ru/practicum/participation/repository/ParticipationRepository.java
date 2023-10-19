package ru.practicum.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.participation.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "where event.id = ?1 "
            + "and event.state = 'PUBLISHED' "
            + "and participation.requestStatus = 'CONFIRMED'")
    List<ParticipationRequest> findAllConfirmedRequestsByEventId(long eventId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "join event.initiator as initiator "
            + "where event.id = ?2 "
            + "and initiator.id = ?1")
    List<ParticipationRequest> findAllRequestsByEventIdAndInitiatorId(long initiatorId, long eventId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "where participation.id IN ?2 "
            + "and event.id = ?1")
    List<ParticipationRequest> findRequestsByIdsList(long eventId, List<Long> requestIds);
}
