package com.thomas15v.ircmc.irc;

import com.thomas15v.ircmc.message.MessageListener;

import java.util.ArrayList;

/**
 * Created by thomas15v on 12/06/15.
 */
public class Chanel {

    private String chanelName;
    private Client client;
    private ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();

    public Chanel(Client client, String chanelName){
        this.client = client;
        this.chanelName = chanelName;
    }

    void pushMessage(Message message){
        for (MessageListener messageListener : messageListeners)
            messageListener.onMessage(message);
    }

    public void sendMessage(String message){
        client.sendMessage(new Message(chanelName, message));
    }

    public void registerListener(MessageListener messageListener){
        messageListeners.add(messageListener);
    }

    public void unRegisterListener(MessageListener messageListener){
        messageListeners.remove(messageListener);
    }

}
