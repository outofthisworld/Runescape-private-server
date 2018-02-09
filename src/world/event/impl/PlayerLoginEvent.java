/*
 Project by outofthisworld24
 All rights reserved.
 */

/*
 * Project by outofthisworld24
 * All rights reserved.
 */

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Project by outofthisworld24
 All rights reserved.
 -----------------------------------------------------------------------------*/

package world.event.impl;

import net.Client;
import net.LoginDecoder;
import world.World;

/**
 * The type Player login event.
 */
public class PlayerLoginEvent extends Event {
    private final World world;
    private final String username;
    private final String password;
    private final Client c;
    private final LoginDecoder decoder;

    /**
     * Instantiates a new Player login event.
     *
     * @param world    the world
     * @param decoder  the decoder
     * @param username the username
     * @param pass     the pass
     * @param c        the c
     */
    public PlayerLoginEvent(World world, LoginDecoder decoder, String username, String pass, Client c) {
        this.world = world;
        this.username = username;
        password = pass;
        this.c = c;
        this.decoder = decoder;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets client.
     *
     * @return the client
     */
    public Client getClient() {
        return c;
    }

    /**
     * Gets world.
     *
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    @Override
    public LoginDecoder getSender() {
        return decoder;
    }
}
