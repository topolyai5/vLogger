package com.topolyai.vlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

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

    public void i(String message) {
        if (configure.logConsole()) {
            Log.i(tag, message);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(message, "INFO"));
        }
    }

    public void w(String message) {
        if (configure.logConsole()) {
            Log.w(tag, message);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(message, "WARN"));
        }
    }

    public void e(String message) {
        if (configure.logConsole()) {
            Log.e(tag, message);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(message, "ERROR"));
        }
    }

    public void e(String message, Throwable tr) {
        if (configure.logConsole()) {
            Log.e(tag, message, tr);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(message, "ERROR"));
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

    public void d(String message) {
        if (configure.logConsole()) {
            Log.d(tag, message);
        }
        if (configure.logFile()) {
            appendLog(formatMsg(message, "DEBUG"));
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
