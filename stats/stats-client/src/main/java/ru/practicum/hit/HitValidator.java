package ru.practicum.hit;

import org.apache.commons.lang3.StringUtils;

public class HitValidator {
    public static boolean validateHitDto(HitDto hitDto) {
        return !StringUtils.isBlank(hitDto.getApp())
                && !StringUtils.isBlank(hitDto.getUri())
                && !StringUtils.isBlank(hitDto.getIp());
    }
}
