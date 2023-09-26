package ru.practicum.hit;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HitDto {
    private long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}
