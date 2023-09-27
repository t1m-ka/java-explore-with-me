package ru.practicum.hit;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public class HitValidator {
    public static boolean validateHitDto(HitDto hitDto) {
        return !StringUtils.isBlank(hitDto.getApp())
                && !StringUtils.isBlank(hitDto.getUri())
                && !StringUtils.isBlank(hitDto.getIp())
                && (hitDto.getTimestamp().isBefore(LocalDateTime.now())
                || hitDto.getTimestamp().isEqual(LocalDateTime.now()));
    }
}
