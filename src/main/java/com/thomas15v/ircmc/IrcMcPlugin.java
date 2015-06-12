package com.thomas15v.ircmc;

import com.thomas15v.ircmc.irc.Chanel;
import com.thomas15v.ircmc.irc.Client;
import com.thomas15v.ircmc.irc.Message;
import com.thomas15v.ircmc.message.MessageListener;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.event.state.ServerStartedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Texts;

import java.io.IOException;


/**
 * Created by thomas15v on 12/06/15.
 */
@Plugin(id = "ircmc", name = "ircmc")
public class IrcMcPlugin implements MessageListener {

    private Client client;
    private Chanel channel;
    private Game game;

    @Subscribe
    public void onEnabled(ServerStartedEvent event){
        game = event.getGame();
        client = new Client("irc.esper.net", "ircmctest2");
        try {
            client.connect();
            channel = client.join("#ircmctest");
            channel.registerListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onChat(PlayerChatEvent event){
        channel.sendMessage(Texts.toPlain(event.getMessage()));
    }


    @Override
    public void onMessage(Message message) {
        game.getServer().getBroadcastSink().sendMessage(Texts.of("<" + message.getTarget() + "> " + message.getMessage()));
    }
}
