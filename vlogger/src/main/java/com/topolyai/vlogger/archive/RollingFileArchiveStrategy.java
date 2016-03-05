package com.topolyai.vlogger.archive;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RollingFileArchiveStrategy implements FileArchiveStrategy {

    //MB
    private int maxSize;

    public RollingFileArchiveStrategy(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean needArchive(File log) {
        long size = log.length();
        long inMb = size / (1024 * 1024);
        return maxSize < inMb;
    }

    @Override
    public String getPostfix() {
        return new SimpleDateFormat("yyMMdd:HHmm", Locale.ENGLISH).format(new Date());
    }


}
