package ru.practicum.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.hit.HitRepository;
import ru.practicum.hit.dto.HitDto;

import static ru.practicum.hit.dto.HitMapper.toHit;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository repository;

    @Override
    public void saveHit(HitDto hitDto) {
        repository.save(toHit(hitDto));
    }
}
