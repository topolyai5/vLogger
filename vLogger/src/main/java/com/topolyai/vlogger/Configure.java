package com.topolyai.vlogger;

import android.content.Context;

import com.topolyai.vlogger.archive.FileArchiveStrategy;

public interface Configure {

    String datePattern();
    
    String msgPattern();
    
    String logPath();
    
    boolean logConsole();

    boolean logFile();

    void createAppenders();

    void rootAppender(Context context);

    int acceptAppender(Class<?> tag, int level);

    FileArchiveStrategy fileArchiveStrategy();
}
