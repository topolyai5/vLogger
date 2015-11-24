package com.topolyai.vlogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    public static String format(String message, Object... args) {

        if (args == null ||args.length == 0) {
            return message;
        }

        Pattern compile = Pattern.compile("\\{\\}");
        Matcher matcher = compile.matcher(message);

        StringBuilder sb = new StringBuilder(message);
        int i = 0;
        boolean out = false;
        while (matcher.find() && !out) {
            if (i < args.length && args[i] != null) {
                sb.replace(matcher.start(), matcher.end(), args[i].toString());
            } else {
                out = true;
            }
            i++;
        }

        return sb.toString();
    }
}
