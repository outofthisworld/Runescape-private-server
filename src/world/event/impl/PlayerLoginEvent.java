package world.event.impl;

import net.impl.decoder.LoginSessionDecoder;
import net.impl.session.Client;
import world.entity.player.Player;

/**
 * The type Player login event.
 */
public class PlayerLoginEvent extends AbstractEvent {
    private final LoginSessionDecoder decoder;
    private final String username;
    private final String password;
    private final Client c;


    /**
     *
     * @param c
     * @param username
     * @param password
     * @param decoder
     */
    public PlayerLoginEvent(Client c,String username, String password,LoginSessionDecoder decoder) {
        this.decoder = decoder;
        this.username = username;
        this.password = password;
        this.c = c;
    }

    public Client getClient(){
        return c;
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


    @Override
    public LoginSessionDecoder getSender() {
        return decoder;
    }
}
