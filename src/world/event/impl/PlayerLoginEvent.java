package world.event.impl;

import net.Client;

public class PlayerLoginEvent extends Event{
    private final String username;
    private final String password;
    private final Client c;

    public PlayerLoginEvent(String username,String pass, Client c){
        this.username = username;
        this.password = pass;
        this.c = c;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Client getClient() {
        return c;
    }

    @Override
    public Client getSender() {
        return c;
    }
}
