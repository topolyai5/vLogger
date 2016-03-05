package com.topolyai.vlogger.archive;

import java.io.File;

public interface FileArchiveStrategy {
    boolean needArchive(File log);
    String getPostfix();
}
