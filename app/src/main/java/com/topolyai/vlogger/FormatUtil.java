package com.topolyai.vlogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {

    public static String format(String message, Object... args) {

        if (args == null ||args.length == 0) {
            return message;
        }

        for (Object arg : args) {
            message = message.replace("{}", arg.toString());
        }

        return message;
    }
}
