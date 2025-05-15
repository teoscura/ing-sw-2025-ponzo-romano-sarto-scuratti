package it.polimi.ingsw.utils;

public enum LoggerLevel {
    OFF     (7), //No messages are ever shown
    ERROR   (6), //Only errors are shown
    WARNING (5), //Only warnings and up are shown
    NOTIF   (4), //Only status changes and upare shown
    SERVER  (3), //Only info from the server and up is shown
    LOBBY   (2), //Only info from every lobby and up is shown
    DEBUG   (1), //Only debug info and up is shown
    ALL     (0); //Every message is shown.

    private int status;

    private LoggerLevel(int status){
        this.status = status;
    }

    public int status(){
        return this.status;
    }

    @Override
    public String toString(){
        switch(this){
            case ALL:       return "[ALL--] ";
            case DEBUG:     return "[DEBUG] ";
            case LOBBY:     return "[LOBBY] ";
            case SERVER:    return "[SERVR] ";
            case NOTIF:     return "[NOTIF] ";
            case ERROR:     return "[ERROR] ";
            case WARNING:   return "[WARN-] ";
            default:       return null;            
        }
    }

}
