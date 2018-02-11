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

package net.impl.decoder;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.impl.enc.ISAACCipher;
import net.impl.session.Client;
import util.RsUtils;
import world.World;
import world.WorldManager;
import world.entity.player.Player;
import world.event.impl.PlayerLoginEvent;

public final class LoginSessionDecoder implements ProtocolDecoder {

    @Override
    public void decode(Client c) {
        int packetOpcode = c.getInBuffer().get() & 0xFF;
        InputBuffer in = new InputBuffer(c.getInBuffer());

        if (packetOpcode != LoginProtocolConstants.NEW_SESSION) {
            return;
        }

        if (in.remaining() < 1) {
            return;
        }

        short loginBlockSize = in.readUnsignedByte();

        if (in.remaining() != loginBlockSize) {
            System.out.println("Not enough data in buffer");
        }


        int encrypedLoginBlockSize = loginBlockSize - LoginProtocolConstants.LOGIN_BLOCK_KEY;

        if (encrypedLoginBlockSize <= 0) {
            System.out.println("Zero RSA packet size");
            return;
        }

        int magicNum = in.readUnsignedByte();

        if (LoginProtocolConstants.LOGIN_MAGIC_NUMBER != magicNum) {
            return;
        }


        int revision = in.readBigUnsignedWORD();

        if (revision != LoginProtocolConstants.PROTOCOL_REVISION) {
            return;
        }

        int lowMemoryVersion = in.readUnsignedByte();

        in.skip(4 * 9);

        encrypedLoginBlockSize--;

        if (in.readUnsignedByte() != encrypedLoginBlockSize) {
            return;
        }

        if (in.readUnsignedByte() != 10) {
            return;
        }


        long clientSeed = in.readBigSignedQWORD();
        long serverSeed = in.readBigSignedQWORD();

        //Client identification key
        in.readBigSignedDWORD();

        String username = RsUtils.readRSString(in);
        String password = RsUtils.readRSString(in);

        boolean validUsername = LoginProtocolConstants.VALID_USERNAME_PREDICATE.test(username);
        boolean validPassword = LoginProtocolConstants.VALID_PASSWORD_PREDICATE.test(password);

        if (!validUsername || !validPassword) {
            sendResponse(c, LoginProtocolConstants.INVALID_USERNAME_OR_PASSWORD, 0);
            return;
        }

        int[] sessionKey = new int[4];
        sessionKey[0] = (int) (clientSeed >> 32);
        sessionKey[1] = (int) clientSeed;
        sessionKey[2] = (int) (serverSeed >> 32);
        sessionKey[3] = (int) serverSeed;

        c.setInCipher(new ISAACCipher(sessionKey));

        for (int i = 0; i < 4; i++) {
            sessionKey[i] += 50;
        }
        c.setOutCipher(new ISAACCipher(sessionKey));

        World world = WorldManager.getWorld(0);


        Player p = new Player(c);
        p.setUsername(username);

        world.getEventBus().fire(new PlayerLoginEvent(p, username, password, this));
    }

    public void sendResponse(Client c, int responseCode, int playerRights) {
        c.write(OutputBuffer.create(3).writeByte(responseCode).writeByte(playerRights).writeByte(0));
    }
}
