package com.topolyai.vlogger;

public interface Configure {

    String datePattern();
    
    String msgPattern();
    
    String logPath();
    
    boolean logConsole();

    boolean logFile();
}
