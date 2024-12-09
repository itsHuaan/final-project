package org.example.final_project.util;

import java.util.regex.Pattern;

public class Const {
    public static final String API_PREFIX = "/v1/api";
    public static final int OTP_LENGTH = 8;
    public static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(regex);
    public static final String GOOGLE = "GOOGLE";
    public static final String FACEBOOK = "FACEBOOK";
}
