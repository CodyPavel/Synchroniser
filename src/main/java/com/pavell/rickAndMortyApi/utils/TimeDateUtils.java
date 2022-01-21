package com.pavell.rickAndMortyApi.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeDateUtils {

    public static LocalDateTime parseDateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime newCreatedDate = LocalDateTime.parse(dateTime, formatter);

        return newCreatedDate;
    }

}
