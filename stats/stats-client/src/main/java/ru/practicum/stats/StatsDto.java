package ru.practicum.stats;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class StatsDto {
    private String app;

    private String uri;

    private int hits;
}
