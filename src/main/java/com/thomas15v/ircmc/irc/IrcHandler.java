package com.thomas15v.ircmc.irc;


import com.google.common.base.Joiner;

import java.util.Arrays;

/**
 * Created by thomas15v on 12/06/15.
 */
public class IrcHandler {

    private Client client;

    public IrcHandler(Client client){
        this.client = client;
    }

    public void onMessage(String sender, IrcCommand command, String... args){
        switch (command) {
            case PING:
                client.sendRaw(IrcCommand.PONG, args);
                break;
            case MODE:
                client.setReady();
                break;
            case PRIVMSG:
                sender = sender.contains("!") ? sender.split("!")[0].substring(1) : sender;
                String message = Joiner.on(" ").join(Arrays.copyOfRange(args, 1, args.length)).substring(1);
                client.getChannels().get(args[0]).pushMessage(new Message(sender, message));
                break;
        }
    }

}
