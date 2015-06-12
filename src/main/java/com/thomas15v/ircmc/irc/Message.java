package com.thomas15v.ircmc.irc;

/**
 * Created by thomas15v on 12/06/15.
 */
public class Message {
    private final String target;
    private final String message;

    public Message(String username, String message){
        this.target = username;
        this.message = message;
    }

    @Override
    public String toString() {
        return target + " : " + message;
    }

    public String getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }
}
