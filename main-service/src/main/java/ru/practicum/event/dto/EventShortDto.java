package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
public class EventShortDto {
    private long id;

    private String annotation;

    private CategoryDto categoryDto;

    private long confirmedRequests;

    private LocalDateTime eventDate;

    private UserShortDto initiatorShortDto;

    private boolean paid;

    private String title;

    private long views;
}
