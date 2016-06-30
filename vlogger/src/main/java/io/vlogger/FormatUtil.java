package io.vlogger;

public class FormatUtil {

    public static String format(String message, Object... args) {
        if (message == null) {
            return "null";
        }
        if (args == null || args.length == 0) {
            return message;
        }

        for (Object arg : args) {
            try {
                message = message.replaceFirst("\\{\\}", arg.toString());
            } catch (Throwable e) {
            }

        }

        return message;
    }
}
