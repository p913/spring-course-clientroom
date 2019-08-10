package ru.ptvi.otuscourse.clientroomapi.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public class DateDiapUtils {
    public static OffsetDateTime dateTimeStartOfDay(LocalDate date) {
        return OffsetDateTime.of(date,
                LocalTime.of(0, 0, 0),
                OffsetDateTime.now().getOffset());
    }

    public static OffsetDateTime dateTimeEndOfDay(LocalDate date) {
        return OffsetDateTime.of(date,
                LocalTime.of(23, 59, 59),
                OffsetDateTime.now().getOffset());
    }
}
