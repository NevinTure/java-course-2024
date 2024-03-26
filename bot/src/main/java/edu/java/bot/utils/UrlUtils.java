package edu.java.bot.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

    private static final Pattern URL_PATTERN = Pattern.compile("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]"
        + "{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)$");
    private static final List<Pattern> ALLOWED_URL_PATTERNS = List.of(
        Pattern.compile("https://github\\.com(\\S+)"),
        Pattern.compile("https://stackoverflow\\.com/questions/(\\d+)/\\S+")
    );

    private UrlUtils() {
    }

    public static boolean isValid(String linkStr) {
        Matcher matcher = URL_PATTERN.matcher(linkStr);
        if (matcher.find()) {
            return validByUrlPattern(linkStr);
        }
        return false;
    }

    private static boolean validByUrlPattern(String linkStr) {
        for (Pattern pattern : ALLOWED_URL_PATTERNS) {
            Matcher matcher = pattern.matcher(linkStr);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }
}
