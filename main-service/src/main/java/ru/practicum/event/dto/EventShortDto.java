package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private String annotation;

    private CategoryDto categoryDto;

    private long confirmedRequests;

    private LocalDateTime eventDate;

    private long id;

    private UserShortDto initiatorShortDto;

    private boolean paid;

    private String title;

    private long views;
}
