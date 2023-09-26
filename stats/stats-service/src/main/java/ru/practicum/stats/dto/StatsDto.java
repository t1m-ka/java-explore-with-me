package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class StatsDto {
    private String app;

    private String uri;

    private long hits;
}
