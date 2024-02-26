package edu.java.bot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

    private static final Pattern URL_PATTERN = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]"
        + "{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)$");


    private UrlUtils() {
    }

    public static boolean isValid(String linkStr) {
        Matcher matcher = URL_PATTERN.matcher(linkStr);
        return matcher.find();
    }
}
