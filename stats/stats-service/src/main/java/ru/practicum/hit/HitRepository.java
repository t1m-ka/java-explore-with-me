package ru.practicum.hit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.hit.model.Hit;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {


    @Query("select new ru.practicum.stats.dto.StatsDto(h.app as app, "
            + "h.uri as uri, "
            + "COUNT(h) as hits) "
            + "FROM Hit h "
            + "WHERE h.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY h.app, h.uri")
    List<StatsDto> getStatsByPeriod(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.stats.dto.StatsDto(h.app as app, "
            + "h.uri as uri, "
            + "COUNT(h) as hits) "
            + "FROM Hit h "
            + "WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 "
            + "GROUP BY h.app, h.uri")
    List<StatsDto> getStatsByPeriodWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.stats.dto.StatsDto(h.app as app, "
            + "h.uri as uri, "
            + "COUNT(DISTINCT h.ip) as hits) "
            + "FROM Hit h "
            + "WHERE h.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY h.app, h.uri")
    List<StatsDto> getStatsByPeriodUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.StatsDto(h.app as app, "
            + "h.uri as uri, "
            + "COUNT(DISTINCT h.ip) as hits) "
            + "FROM Hit h "
            + "WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 "
            + "GROUP BY h.app, h.uri")
    List<StatsDto> getStatsByPeriodWithUriUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
//List<StatsDto> findByTimestampAfterAndTimestampBefore(LocalDateTime start, LocalDateTime end);

//List<StatsDto> findByTimestampAfterAndTimestampBeforeAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

//List<StatsDto> findByTimestampAfterAndTimestampBeforeAndDistinctByIp(LocalDateTime start, LocalDateTime end);

//List<StatsDto> findByTimestampAfterAndTimestampBeforeAndUriInAndDistinctByIp(LocalDateTime start, LocalDateTime end, List<String> uris);