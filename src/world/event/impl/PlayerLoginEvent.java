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

import net.impl.LoginDecoder;
import world.entity.player.Player;

/**
 * The type Player login event.
 */
public class PlayerLoginEvent extends Event {
    private final LoginDecoder decoder;
    private final Player player;


    /**
     * Instantiates a new Player login event.
     *
     * @param p       the p
     * @param decoder the decoder
     */
    public PlayerLoginEvent(Player p, LoginDecoder decoder) {
        player = p;
        this.decoder = decoder;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public LoginDecoder getSender() {
        return decoder;
    }
}
