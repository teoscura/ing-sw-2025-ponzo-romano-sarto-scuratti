package it.polimi.ingsw.utils;

import java.io.PrintStream;

public class Logger {
    static private Logger instance = null;

    private final Object stream_lock;
    private PrintStream out;
    private LoggerLevel level = LoggerLevel.NOTIF;

    private Logger(){
        this.out = System.out;
        this.stream_lock = new Object();
    }

    static public Logger getInstance(){
        if (instance==null) instance = new Logger();
        return instance;
    }

    static public void reset(){
        instance = null;
    }

    public void setLevel(LoggerLevel level){
        this.level = level;
    }

    public void setStream(PrintStream stream){
        this.out = stream;
    }

    public void print(String message, LoggerLevel level){
        if(level.status()>this.level.status()) return;
        synchronized(stream_lock){
            out.println(level+message);
        }
    }

}
