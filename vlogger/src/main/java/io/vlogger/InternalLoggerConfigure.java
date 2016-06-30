package io.vlogger;

final class InternalLoggerConfigure extends BaseLoggerConfigure {
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
