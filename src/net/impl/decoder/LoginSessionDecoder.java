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
import world.entity.player.Player;
import world.event.impl.PlayerLoginEvent;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoginSessionDecoder implements ProtocolDecoder {
    private static final Logger logger = Logger.getLogger(LoginSessionDecoder.class.getName());
    private static final Random random =  new Random(System.nanoTime());
    int count = 0;
    @Override
    public void decode(Client c) {
        InputBuffer in = c.getInputBuffer();

        if (in.remaining() < 1) {
            return;
        }

        int packetOpcode = in.readUnsignedByte();


        if (packetOpcode != LoginProtocolConstants.NEW_SESSION) {
            return;
        }


        int loginBlockSize = in.readUnsignedByte();

        if (in.remaining() != loginBlockSize) {
            System.out.println("not enuf remaining");
            return;
        }


        int encrypedLoginBlockSize = loginBlockSize - LoginProtocolConstants.LOGIN_BLOCK_KEY;

        if (encrypedLoginBlockSize <= 0) {
            System.out.println("login block size < 0");
            return;
        }

        int magicNum = in.readUnsignedByte();

        if (LoginProtocolConstants.LOGIN_MAGIC_NUMBER != magicNum) {
            System.out.println("invalid magic num");
            return;
        }


        int revision = in.readBigUnsignedWord();
        if (revision != LoginProtocolConstants.PROTOCOL_REVISION) {
            System.out.println("invalid revision");
            return;
        }

        int lowMemoryVersion = in.readUnsignedByte();



        in.skip(4 * 9);

        encrypedLoginBlockSize--;

        int size = in.readUnsignedByte();
        System.out.println("size was : " + size);
        System.out.println("enc was: " + encrypedLoginBlockSize);
        //if (size != encrypedLoginBlockSize) {
           // System.out.println("invalid encrypted login block size");
          //  return;
       // }

       int constant = in.readUnsignedByte();
       // if (constant != 10) {
        //    System.out.println("not 10 " + constant);
        //    return;
      //  }


        long clientSeed = in.readBigSignedQWORD();
        long serverSeed = in.readBigSignedQWORD();

        //Client identification key
        in.readBigSignedDWORD();

        String username = RsUtils.readRSString(in);
        String password = RsUtils.readRSString(in);


        boolean validUsername = LoginProtocolConstants.VALID_USERNAME_PREDICATE.test(username);
        boolean validPassword = LoginProtocolConstants.VALID_PASSWORD_PREDICATE.test(password);

        if ((!validUsername || !validPassword)) {
            System.out.println("not valid username or pass");
            username = "bobdo" + random.nextInt(2000);
            password = "bobdo" + random.nextInt(2000);
           // sendResponse(c, LoginProtocolConstants.INVALID_USERNAME_OR_PASSWORD, 0);
            //return;
        }

        int[] sessionKey = new int[4];
        sessionKey[0] = (int) (clientSeed >> 32);
        sessionKey[1] = (int) clientSeed;
        sessionKey[2] = (int) (serverSeed >> 32);
        sessionKey[3] = (int) serverSeed;

        c.setInCipher(new ISAACCipher(sessionKey));

        for (int i = 0; i < sessionKey.length; i++) {
            sessionKey[i] += 50;
        }
        c.setOutCipher(new ISAACCipher(sessionKey));

        Player p = new Player();
        p.setUsername(username);
        p.setClient(c);
        p.getPosition().getVector().setX(p.getPosition().getVector().getX() + random.nextInt(80));
        p.getPosition().getVector().setY(p.getPosition().getVector().getY() + random.nextInt(80));
        LoginSessionDecoder.logger.log(Level.INFO, "Firing player login event");
        p.send(new PlayerLoginEvent(p, username, password, this));
    }

    public void sendResponse(Client c, int responseCode, int playerRights) {
        c.write(OutputBuffer.create(3).writeByte(responseCode).writeByte(playerRights).writeByte(0));
    }
}
