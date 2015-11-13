package com.topolyai.vlogger.appender;

public abstract class AbstractAppender implements Appender {

    protected int level;
    protected String name;

    public AbstractAppender(int level, String name) {
        this.level = level;
        this.name = name;
    }


    @Override
    public boolean accept(Class<?> clzz, int level) {
        return clzz.getPackage().getName().startsWith(name) && this.level <= level;
    }
}
