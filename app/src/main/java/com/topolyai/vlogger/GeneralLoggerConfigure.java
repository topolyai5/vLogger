package com.topolyai.vlogger;

public class GeneralLoggerConfigure extends BaseLoggerConfigure {
    @Override
    public String logPath() {
        return null;
    }

    @Override
    public boolean logFile() {
        return false;
    }

    @Override
    public void createAppenders() {

    }
}
