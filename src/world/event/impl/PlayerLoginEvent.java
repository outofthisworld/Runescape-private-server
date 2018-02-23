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

import net.impl.decoder.LoginSessionDecoder;
import world.entity.player.Player;

/**
 * The type Player login event.
 */
public class PlayerLoginEvent extends AbstractEvent {
    private final LoginSessionDecoder decoder;
    private final Player player;
    private final String username;
    private final String password;


    /**
     * Instantiates a new Player login event.
     *
     * @param p        the p
     * @param username the username
     * @param password the password
     * @param decoder  the decoder
     */
    public PlayerLoginEvent(Player p, String username, String password, LoginSessionDecoder decoder) {
        player = p;
        this.decoder = decoder;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets decoder.
     *
     * @return the decoder
     */
    public LoginSessionDecoder getDecoder() {
        return decoder;
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
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public LoginSessionDecoder getSender() {
        return decoder;
    }
}
