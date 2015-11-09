package com.topolyai.vlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import static java.lang.String.format;

public class Logger {

    private static Configure configure;

    private String tag;

    public static void create(Configure configure) {
        if (Logger.configure == null) {
            Logger.configure = configure;
        } else {
            throw new IllegalArgumentException("Cannot initialized the Logger twice.");
        }
    }

    private Logger(Class<?> clazz) {
        tag = clazz.getSimpleName();
    }

    public void i(String message, Object ... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.i(tag, formatted);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(formatted, "INFO"));
        }
    }

    public void w(String message, Object ... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.w(tag, formatted);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(message, "WARN"));
        }
    }

    public void e(String message, Object ... args) {

        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.e(tag, formatted);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(formatted, "ERROR"));
        }
    }

    public void e(String message, Throwable tr, Object ... args) {
        String msg;
        if (!TextUtils.isEmpty(tr.getMessage())) {
            msg = tr.getMessage();
        } else if (tr.getCause() != null && !TextUtils.isEmpty(tr.getCause().getMessage())) {
            msg = tr.getCause().getMessage();
        } else {
            msg = "Unexpected error, see stacktrace.";
        }
        String formatted = format(msg, args);
        if (configure.logConsole()) {
            Log.e(tag, formatted, tr);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(formatted, "ERROR"));
            appendLog(Log.getStackTraceString(tr));
        }
    }

    private String formatMsg(String message, String level) {
        String msgPattern = configure.msgPattern();
        String msg = msgPattern.replaceAll("%d", formatDate()).replaceAll("%l", level).replaceAll("%m", message);
        return msg;
    }

    private String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(configure.datePattern(), Locale.getDefault());
        return sdf.format(System.currentTimeMillis());
    }

    public void d(String message, Object ... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.d(tag, formatted);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(formatted, "DEBUG"));
        }
    }

    private void appendLog(String text) {
        if (!configure.logPath().isEmpty()) {
            File logFile = new File(Environment.getExternalStorageDirectory() + configure.logPath());
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger get(Class<?> clazz) {
        return new Logger(clazz);
    }

    public static String logFile() {
        return Environment.getExternalStorageDirectory() + configure.logPath();
    }

}
