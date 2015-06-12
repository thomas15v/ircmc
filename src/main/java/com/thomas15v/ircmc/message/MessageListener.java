package com.thomas15v.ircmc.message;

import com.thomas15v.ircmc.irc.Message;

/**
 * Created by thomas15v on 12/06/15.
 */
public interface MessageListener {

    public void onMessage(Message message);

}
