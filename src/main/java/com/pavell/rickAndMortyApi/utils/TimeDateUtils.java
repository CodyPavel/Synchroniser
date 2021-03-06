package com.pavell.rickAndMortyApi.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@UtilityClass
public final class TimeDateUtils {
    public static LocalDateTime parseDateTime(String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            LocalDateTime newCreatedDate = LocalDateTime.parse(dateTime, formatter);

            return newCreatedDate;

        } catch (Exception e) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime newCreatedDate = LocalDateTime.parse(dateTime, formatter);

                return newCreatedDate;
            } catch (Exception exception) {
                throw new RuntimeException("Can not format date and time: {" + dateTime + "} to LocalDateTime(ISO_DATE_TIME)");
            }
        }
    }

    public static Date parseDate(String date) throws ParseException {
        try {
            DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            Date data = format.parse(date);
            return data;
        } catch (Exception e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                Date data = format.parse(date);
                return data;
            } catch (Exception exception) {
                throw new RuntimeException("Can not format date: {" + date + "} to SimpleDateFormat(yyyy-MM-dd HH:mm:ss.S)");
            }
        }
    }
}
