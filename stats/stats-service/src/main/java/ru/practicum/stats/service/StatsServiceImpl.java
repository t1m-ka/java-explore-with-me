package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.practicum.hit.HitRepository;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatsDto> statsDtoList;
        if (unique) {
            if (CollectionUtils.isNotEmpty(uris)) {
                statsDtoList = hitRepository.getStatsByPeriodWithUriUniqueIp(start, end, uris);
            } else {
                statsDtoList = hitRepository.getStatsByPeriodUniqueIp(start, end);
            }
        } else {
            if (CollectionUtils.isNotEmpty(uris)) {
                statsDtoList = hitRepository.getStatsByPeriodWithUri(start, end, uris);
            } else {
                statsDtoList = hitRepository.getStatsByPeriod(start, end);
            }
        }
        return statsDtoList;
    }
}
