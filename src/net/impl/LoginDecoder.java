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

package net.impl;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.enc.ISAACCipher;
import util.RsUtils;
import world.World;
import world.WorldManager;
import world.entity.player.Player;
import world.event.impl.PlayerLoginEvent;

public final class LoginDecoder implements ProtocolDecoder {
    private static final int LOGIN_REQUEST = 14;
    private static final int UPDATE = 15;
    private static final int NEW_SESSION = 16;
    private static final int RECONNECT = 18;

    @Override
    public void decode(Client c) {
        int packetOpcode = c.getInBuffer().get() & 0xFF;
        InputBuffer in = new InputBuffer(c.getInBuffer());

        switch (packetOpcode) {
            case LoginDecoder.LOGIN_REQUEST:
                System.out.println("in login stage one");

                /*if (in.readUnsignedByte() != 14) {
                    System.out.println("invalid login byte / supposed to be 14");
                    return;
                }*/

                //Name hash
                in.readUnsignedByte();

                c.write(OutputBuffer.create(73, 10)
                        .writeBigQWORD(0)
                        .writeByte(0)// login response - 0 means exchange session key to establish encryption
                        .writeBigQWORD(c.getServerSessionKey()));// send the net.Reactor part of the session Id used (Client+net.Reactor part together are used as cryption key
                break;
            case LoginDecoder.NEW_SESSION:
            case LoginDecoder.RECONNECT:

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


                Player p = new Player();
                p.setUsername(username);
                p.setPassword(password);
                p.setRights(0);
                p.setClient(c);

                world.getEventBus().fire(new PlayerLoginEvent(p, this));
                break;
        }
    }

    public void sendResponse(Client c, int responseCode, int playerRights) {
        c.write(OutputBuffer.create(3).writeByte(responseCode).writeByte(playerRights).writeByte(3));
    }
}
