package io.vlogger;

import android.content.Context;

import io.vlogger.appender.Appender;
import io.vlogger.appender.RootAppender;
import io.vlogger.archive.FileArchiveStrategy;
import io.vlogger.archive.RollingFileArchiveStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseLoggerConfigure implements Configure {

    private List<Appender> appenders = new ArrayList<>();

    @Override
    public String datePattern() {
        return "yyyy-MM-dd hh:mm:ss";
    }

    @Override
    public String msgPattern() {
        return "%d - %c [%l]: %m";
    }

    @Override
    public abstract String logPath();

    @Override
    public boolean logConsole() {
        return true;
    }

    @Override
    public boolean logFile() {
        return true;
    }

    protected void addAppender(Appender appender) {
        appenders.add(appender);
    }

    public int acceptAppender(Class<?> tag, int level) {
        int ret = 6;
        for (Appender appender : appenders) {
            if (appender.accept(tag, level)) {
                if (level < ret) {
                    ret = level;
                }
            }
        }
        return ret;
    }

    public void rootAppender(Context context) {
        addAppender(new RootAppender(getRootLevel(), context.getPackageName()));
    }

    protected int getRootLevel() {
        return Appender.INFO;
    }

    @Override
    public FileArchiveStrategy fileArchiveStrategy() {
        return new RollingFileArchiveStrategy(30);
    }


}
