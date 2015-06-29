package com.topolyai.vlogger;

public abstract class BaseLoggerConfigure implements Configure {

    @Override
    public String datePattern() {
        return "yyyy-MM-dd hh:mm:ss";
    }

    @Override
    public String msgPattern() {
        return "%d  %l %m";
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
}
