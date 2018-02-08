package world.event.impl;

import net.Client;
import net.LoginDecoder;
import world.World;

public class PlayerLoginEvent extends Event {
    private final World world;
    private final String username;
    private final String password;
    private final Client c;
    private final LoginDecoder decoder;

    public PlayerLoginEvent(World world, LoginDecoder decoder, String username, String pass, Client c) {
        this.world = world;
        this.username = username;
        password = pass;
        this.c = c;
        this.decoder = decoder;
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

    public World getWorld() {
        return world;
    }

    @Override
    public LoginDecoder getSender() {
        return decoder;
    }
}
