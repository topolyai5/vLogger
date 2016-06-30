package io.vlogger;

import android.content.Context;

import io.vlogger.archive.FileArchiveStrategy;

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
