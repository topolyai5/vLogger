package com.topolyai.vlogger;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.topolyai.vlogger.appender.Appender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.topolyai.vlogger.FormatUtil.format;

public class Logger {

    private static Configure configure;

    private Class<?> tag;

    public static void create(Configure configure, Context context) {
        if (Logger.configure == null) {
            Logger.configure = configure;
            configure.createAppenders();
            configure.rootAppender(context);
        } else {
            Log.i("Logger", "Cannot initialized the Logger twice.");
        }
    }

    private Logger(Class<?> clazz) {
        tag = clazz;
    }

    public void t(String message, Object... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.v(tag.getSimpleName(), formatted);
        }
        appendLog(formatMsg(formatted, "VERBOSE"), Appender.VERBOSE);
    }

    public void d(String message, Object... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.d(tag.getSimpleName(), formatted);
        }
        appendLog(formatMsg(formatted, "DEBUG"), Appender.DEBUG);
    }

    public void i(String message, Object... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.i(tag.getSimpleName(), formatted);
        }
        appendLog(formatMsg(formatted, "INFO"), Appender.INFO);
    }

    public void w(String message, Object... args) {
        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.w(tag.getSimpleName(), formatted);
        }
        appendLog(formatMsg(message, "WARN"), Appender.WARN);
    }

    public void e(String message, Object... args) {

        String formatted = format(message, args);
        if (configure.logConsole()) {
            Log.e(tag.getSimpleName(), formatted);
        }
        appendLog(formatMsg(formatted, "ERROR"), Appender.ERROR);
    }

    public void e(String message, Throwable tr, Object... args) {
        String msg;
        if (!TextUtils.isEmpty(message)) {
            msg = message;
        } else if (!TextUtils.isEmpty(tr.getMessage())) {
            msg = tr.getMessage();
        } else if (tr.getCause() != null && !TextUtils.isEmpty(tr.getCause().getMessage())) {
            msg = tr.getCause().getMessage();
        } else {
            msg = "Unexpected error, see stacktrace.";
        }
        String formatted = format(msg, args);
        if (configure.logConsole()) {
            Log.e(tag.getSimpleName(), formatted, tr);
        }
        appendLog(formatMsg(formatted, "ERROR"), Appender.ERROR, Log.getStackTraceString(tr));
    }

    public void f(String message, Object... args) {
        String formatted = format(message, args);
        appendLog(formatMsg(formatted, "FATAL"), Appender.FATAL);
    }

    private String formatMsg(String message, String level) {
        return configure.msgPattern().replaceAll("%%d%%", formatDate()).replaceAll("%%l%%", level).replaceAll("%%m%%", message.replace("$", "#"));
    }

    private String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(configure.datePattern(), Locale.getDefault());
        return sdf.format(System.currentTimeMillis());
    }

    private void appendLog(String text, int level) {
        appendLog(text, level, null);
    }

    private void appendLog(String text, int level, String stackTrace) {
        int l = configure.acceptAppender(tag, level);
        if (!TextUtils.isEmpty(configure.logPath()) && configure.logFile() && l <= level) {
            File logFile = new File(Environment.getExternalStorageDirectory() + configure.logPath());

            if (configure.fileArchiveStrategy().needArchive(logFile)) {
                logFile.renameTo(new File(logFile + "_" + configure.fileArchiveStrategy().getPostfix()));
                logFile.delete();
            }

            if (!logFile.exists()) {
                try {
                    if (!logFile.getParentFile().exists()) {
                        boolean success = logFile.getParentFile().mkdirs();
                        if (!success) {
                            Log.w("Logger", "Directory doesn't created.");
                        }
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    Log.w("Logger", e.getMessage());
                }
            }
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(text);
                buf.newLine();
                if (!TextUtils.isEmpty(stackTrace)) {
                    buf.append(stackTrace);
                    buf.newLine();
                }
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
