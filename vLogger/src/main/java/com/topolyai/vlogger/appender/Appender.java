package com.topolyai.vlogger.appender;

public interface Appender {
    int VERBOSE = 0;
    int DEBUG = 1;
    int INFO = 2;
    int WARN = 3;
    int ERROR = 4;
    int FATAL = 5;

    boolean accept(Class<?> clzz, int level);
}
