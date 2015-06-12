package com.thomas15v.ircmc.irc;

/**
 * Created by thomas15v on 12/06/15.
 */
public enum IrcCommand {
    PING,
    PONG,
    USER,
    NICK,
    PRIVMSG,
    MODE,
    JOIN;

    public static boolean contains(String string){
        try {
            IrcCommand.valueOf(string);
        } catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }
}
