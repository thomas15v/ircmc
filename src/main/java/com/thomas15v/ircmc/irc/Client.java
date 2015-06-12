package com.thomas15v.ircmc.irc;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Client implements Runnable {

    private String host;
    private int port;
    private String username;
    private PrintStream out;
    private BufferedReader in;
    private Socket socket;
    private Thread thread;
    private IrcHandler handler;
    private Map<String, Chanel> channels = new HashMap<String, Chanel>();
    private CountDownLatch readyLatch;

    public Client(String host, String username){
        this(host, 6667, username);
    }

    public Client(String host, int port, String username){
        this.host = host;
        this.port = port;
        this.username = username;
        this.handler = new IrcHandler(this);
    }

    public void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        thread = new Thread(this);
        readyLatch = new CountDownLatch(1);
        thread.start();
        register();
    }

    public Chanel join(String chanel){
        try {
            readyLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!chanel.startsWith("#"))
            chanel = "#" + chanel;
        sendRaw(IrcCommand.JOIN, chanel);
        Chanel chanelobj = new Chanel(this, chanel);
        channels.put(chanel, chanelobj);
        return chanelobj;
    }

    public void sendRaw(String message){
        System.out.println("send " + message);
        out.println(message);
        out.flush();
    }

    public void sendMessage(Message message){
        sendRaw(IrcCommand.PRIVMSG, message.getTarget(), ":" + message.getMessage());
    }

    public void sendRaw(IrcCommand command, String... args){
        StringBuilder builder = new StringBuilder();
        builder.append(command.name() + " ");
        for (String arg : args)
            builder.append(arg + " ");
        sendRaw(builder.toString());
    }

    private void register(){
        sendRaw(IrcCommand.USER, username, "localhost", host, username);
        sendRaw(IrcCommand.NICK, username);
    }



    public void disconnect() throws IOException {
        socket.close();
        thread.interrupt();
    }

    void setReady(){
        readyLatch.countDown();
    }

    public Map<String, Chanel> getChannels() {
        return channels;
    }

    @Override
    public void run() {
        while (true){
            try
            {
                String msg;
                while ( ( msg = in.readLine() ) != null )
                {
                    String sender = "SERVER";
                    System.out.println("recieved :" + msg);
                    if (msg.startsWith(":")) {
                        int firstspace = msg.indexOf(" ");
                        sender = msg.substring(0, firstspace);
                        msg = msg.substring(firstspace + 1, msg.length());
                    }
                    int firstspace = msg.indexOf(" ");
                    String command = msg.substring(0, firstspace);
                    if (IrcCommand.contains(command)) {
                        String[] args = msg.substring(firstspace + 1, msg.length()).split(" ");
                        handler.onMessage(sender, IrcCommand.valueOf(command), args);
                    }
                }
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}
