package org.example.final_project.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.regex.Pattern;

public class Const {
    public static final String API_PREFIX = "/v1/api";
    public static final int OTP_LENGTH = 8;
    public static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(regex);
    public static final String GOOGLE = "GOOGLE";
    public static final String FACEBOOK = "FACEBOOK";
    private static final LocalDate today = LocalDate.now();
    private static final LocalTime startHour = LocalTime.of(0, 0, 0);
    public static final LocalDateTime START_OF_DAY = LocalDateTime.of(today, startHour);
    public static final LocalDateTime END_OF_DAY = LocalDateTime.of(today, LocalTime.of(23, 59, 59));
    public static final LocalDateTime START_OF_WEEK = START_OF_DAY.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    public static final LocalDateTime START_OF_MONTH = START_OF_DAY.with(TemporalAdjusters.firstDayOfMonth());
    public static final LocalDateTime START_OF_YEAR = START_OF_DAY.with(TemporalAdjusters.firstDayOfYear());
}
