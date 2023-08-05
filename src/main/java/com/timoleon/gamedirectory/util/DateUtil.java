package com.timoleon.gamedirectory.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_PATTERN_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static Date stringToDate(final String date, final SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static Calendar localDateToCalendar(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return calendar;
    }

    public static List<LocalDate> getMonthPeriodsFromDateRange(LocalDate from, LocalDate to) {
        List<LocalDate> results = new ArrayList<LocalDate>();
        Calendar start = DateUtil.localDateToCalendar(from);
        Calendar end = DateUtil.localDateToCalendar(to);

        if (end.get(Calendar.MONTH) - start.get(Calendar.MONTH) < 1) {
            // Same month, add the period as is
            results.add(from);
            results.add(to);
        } else {
            results.add(from);
            while (start.compareTo(end) <= 0) {
                // Add the last date of month
                start.add(Calendar.DATE, (start.getActualMaximum(Calendar.DAY_OF_MONTH) - start.get(Calendar.DATE)));
                if (start.compareTo(end) <= 0) {
                    results.add(LocalDateTime.ofInstant(start.toInstant(), start.getTimeZone().toZoneId()).toLocalDate());
                }

                // Add the first date of next month
                start.add(Calendar.MONTH, 1);
                start.set(Calendar.DAY_OF_MONTH, start.getActualMinimum(Calendar.DAY_OF_MONTH));
                if (start.compareTo(end) <= 0) {
                    results.add(LocalDateTime.ofInstant(start.toInstant(), start.getTimeZone().toZoneId()).toLocalDate());
                }
            }

            if (results.size() % 2 != 0) {
                results.add(to);
            }
        }

        return results;
    }

    public static Calendar stringToCalendar(String date, SimpleDateFormat dateFormat) {
        Calendar cal = null;
        if (StringUtils.hasLength(date) && dateFormat != null) {
            try {
                cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return cal;
    }

    public static Integer calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}
