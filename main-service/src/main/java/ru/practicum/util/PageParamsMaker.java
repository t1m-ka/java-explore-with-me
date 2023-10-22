package ru.practicum.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageParamsMaker {
    public static Pageable makePageable(int from, int size) {
        int pageNumber = from / size;
        return PageRequest.of(pageNumber, size);
    }

    public static Pageable makePageableWithSort(int from, int size, Sort sort) {
        int pageNumber = from / size;
        return PageRequest.of(pageNumber, size, sort);
    }
}
