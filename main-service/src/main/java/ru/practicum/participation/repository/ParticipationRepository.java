package ru.practicum.participation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.participation.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "where event.id = ?1 "
            + "and event.state = 'PUBLISHED'")
    List<ParticipationRequest> findAllPublishedEventsById(long eventId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "join event.initiator as initiator "
            + "where event.id = ?1 "
            + "and initiator.id = ?2")
    List<ParticipationRequest> findAllRequestsByEventIdAndInitiatorId(long initiatorId, long eventId);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "join event.initiator as initiator "
            + "where event.id IN ?1 "
            + "and initiator.id = ?2")
    List<ParticipationRequest> findUserRequestsByEventIdsList(long initiatorId, List<Long> eventIds);

    @Query("select participation "
            + "from ParticipationRequest as participation "
            + "join participation.event as event "
            + "where event.id = ?1 "
            + "and participation.requestStatus = 'CONFIRMED'")
    int countConfirmedRequestsByEventId(long eventId);
}
