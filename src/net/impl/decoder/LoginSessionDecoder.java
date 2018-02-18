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

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoginSessionDecoder implements ProtocolDecoder {
    private static final Logger logger = Logger.getLogger(LoginSessionDecoder.class.getName());

    @Override
    public void decode(Client c) {

        LoginSessionDecoder.logger.log(Level.INFO, "Decoding login session");

        InputBuffer in = c.getInputBuffer();

        if (in.remaining() < 1) {
            LoginSessionDecoder.logger.log(Level.INFO, "Invalid login request");
            return;
        }

        int packetOpcode = in.readUnsignedByte();
        LoginSessionDecoder.logger.log(Level.INFO, "Login session packetOpcode " + packetOpcode);


        if (packetOpcode != LoginProtocolConstants.NEW_SESSION) {
            LoginSessionDecoder.logger.log(Level.INFO, "Unhandled " + packetOpcode);
            return;
        }

        int loginBlockSize = in.readUnsignedByte();
        LoginSessionDecoder.logger.log(Level.INFO, "Login block size:  " + loginBlockSize);

        if (in.remaining() != loginBlockSize) {
            LoginSessionDecoder.logger.log(Level.INFO, "Not enough data in buffer to reach login block size: :  " + loginBlockSize);
            return;
        }


        int encrypedLoginBlockSize = loginBlockSize - LoginProtocolConstants.LOGIN_BLOCK_KEY;
        LoginSessionDecoder.logger.log(Level.INFO, "Encrypted login block size:   " + encrypedLoginBlockSize);

        if (encrypedLoginBlockSize <= 0) {
            LoginSessionDecoder.logger.log(Level.INFO, "Zero rsa length  ");
            return;
        }

        int magicNum = in.readUnsignedByte();
        LoginSessionDecoder.logger.log(Level.INFO, "Magic num: :  " + magicNum);

        if (LoginProtocolConstants.LOGIN_MAGIC_NUMBER != magicNum) {
            LoginSessionDecoder.logger.log(Level.INFO, "Invalid magic num:  " + magicNum);
            return;
        }


        int revision = in.readBigUnsignedWORD();
        LoginSessionDecoder.logger.log(Level.INFO, "Revision:  " + revision);
        if (revision != LoginProtocolConstants.PROTOCOL_REVISION) {
            LoginSessionDecoder.logger.log(Level.INFO, "Invalid revision:  " + revision);
            return;
        }

        int lowMemoryVersion = in.readUnsignedByte();

        in.skip(4 * 9);

        encrypedLoginBlockSize--;

        if (in.readUnsignedByte() != encrypedLoginBlockSize) {
            LoginSessionDecoder.logger.log(Level.INFO, "Invalid enc block size");
            return;
        }

        if (in.readUnsignedByte() != 10) {
            LoginSessionDecoder.logger.log(Level.INFO, "Invalid no: 10  ");
            return;
        }


        long clientSeed = in.readBigSignedQWORD();
        LoginSessionDecoder.logger.log(Level.INFO, "Read client seed  " + clientSeed);
        long serverSeed = in.readBigSignedQWORD();
        LoginSessionDecoder.logger.log(Level.INFO, "Read server seed  " + serverSeed);

        //Client identification key
        in.readBigSignedDWORD();

        String username = RsUtils.readRSString(in);
        LoginSessionDecoder.logger.log(Level.INFO, "Username :  " + username);
        String password = RsUtils.readRSString(in);
        LoginSessionDecoder.logger.log(Level.INFO, "Password :  " + password);


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

        System.out.println(sessionKey[0]);
        System.out.println(sessionKey[1]);
        System.out.println(sessionKey[2]);
        System.out.println(sessionKey[3]);

        c.setInCipher(new ISAACCipher(sessionKey));

        for (int i = 0; i < sessionKey.length; i++) {
            sessionKey[i] += 50;
        }
        c.setOutCipher(new ISAACCipher(sessionKey));

        World world = WorldManager.getWorld(0);


        Player p = new Player();
        p.setUsername(username);
        p.setClient(c);

        LoginSessionDecoder.logger.log(Level.INFO, "Firing player login event");
        world.getEventBus().fire(new PlayerLoginEvent(p, username, password, this));
    }

    public void sendResponse(Client c, int responseCode, int playerRights) {
        c.write(OutputBuffer.create(3).writeByte(responseCode).writeByte(playerRights).writeByte(0));
    }
}
