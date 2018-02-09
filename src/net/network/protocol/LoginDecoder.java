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

package net.network.protocol;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.enc.ISAACCipher;
import net.network.Client;
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

        if (c.isLoggedIn()) {
            return;
        }

        switch (packetOpcode) {
            case LoginDecoder.LOGIN_REQUEST:
                System.out.println("in login stage one");

                /*if (in.readUnsignedByte() != 14) {
                    System.out.println("invalid login byte / supposed to be 14");
                    return;
                }*/

                //Name hash
                in.readUnsignedByte();

                c.write(OutputBuffer.create(73, 10).writeBytes(0, 8)// is being ignored by the Client
                        .writeByte(0)// login response - 0 means exchange session key to establish encryption
                        .writeBigQWORD(c.getServerSessionKey()));// send the net.Reactor part of the session Id used (Client+net.Reactor part together are used as cryption key
                break;
            case LoginDecoder.NEW_SESSION:
            case LoginDecoder.RECONNECT:
                /*
                int loginType = in.readUnsignedByte();
                if (loginType != 16 && loginType != 18) {
                    System.out.println("Wrong login type");
                    return;
                }*/

                if (in.remaining() < 1) {
                    return;
                }

                short loginPacketSize = in.readUnsignedByte();
                System.out.println(loginPacketSize);
                System.out.println(in.remaining());

                if (in.remaining() != loginPacketSize) {
                    System.out.println("Not enough data in buffer");
                }


                int loginEncryptedPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
                System.out.println(loginEncryptedPacketSize);

                if (loginEncryptedPacketSize <= 0) {
                    System.out.println("Zero RSA packet size");
                    return;
                }

                System.out.println("Remaining in buffer after stage 2: " + in.remaining());

                int magicNum = in.readUnsignedByte();
                int revision = in.readBigUnsignedWORD();

                System.out.println(magicNum);
                System.out.println(revision);

                if (magicNum != 255 || revision != 317) {
                    System.out.println("Invalid magic num or revision");
                    return;
                }

                int lowMemoryVersion = in.readUnsignedByte();
                in.skip(4 * 9);

                loginEncryptedPacketSize--;
                if (in.readUnsignedByte() != loginEncryptedPacketSize) {
                    System.out.println("inv");
                    return;
                }


                if (in.readUnsignedByte() != 10) {
                    System.out.println("byte wasnt 10");
                    return;
                }

                long clientSessionKey = in.readBigSignedQWORD();
                long serverSessionKey = in.readBigSignedQWORD();

                int userID = in.readBigSignedDWORD();

                byte[] usernameBytes = in.readUntil(b -> b.byteValue() == 10);

                if (usernameBytes == null) {
                    System.out.println("Username bytes null");
                    return;
                }

                String username = new String(usernameBytes, 0, usernameBytes.length - 1);
                if (username == null || username.length() == 0) {
                    System.out.println("Blank username");
                    return;
                }

                byte[] passwordBytes = in.readUntil(b -> b.byteValue() == 10);

                if (passwordBytes == null) {
                    System.out.println("password bytes null");
                }

                String password = new String(passwordBytes);

                if (password == null || password.length() == 0) {
                    System.out.println("invalid pass");
                    return;
                }

                System.out.println(username);
                System.out.println(password);

                int[] sessionKey = new int[4];
                sessionKey[0] = (int) (clientSessionKey >> 32);
                sessionKey[1] = (int) clientSessionKey;
                sessionKey[2] = (int) (serverSessionKey >> 32);
                sessionKey[3] = (int) serverSessionKey;


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

                world.getEventBus().fire(new PlayerLoginEvent(p, new LoginDecoder()));
                /**
                 1	Waits for 2000ms and tries again while counting failures.
                 0	Exchanges session keys, entity name, password, etc.
                 1	Waits for 2000ms and tries again.
                 2	Client made a successful login.
                 3	"Invalid username or password."
                 4	"Your account has been disabled. Please check your message-center for details."
                 5	"Your account is already logged in. Try again in 60 secs..."
                 6	"RuneScape has been updated! Please reload this page."
                 7	"This world is full. Please use a different world."
                 8	"Unable to connect. Login server offline."
                 9	"Login limit exceeded. Too many connections from your address."
                 10	"Unable to connect. Bad session id."
                 11	"Login server rejected session. Please try again."
                 12	"You need a members account to login to this world. Please subscribe, or use a different world."
                 13	"Could not complete login. Please try using a different world."
                 14	"The server is being updated. Please wait 1 minute and try again."
                 15	See the notes below.
                 16	"Login attempts exceeded. Please wait 1 minute and try again."
                 17	"You are standing in a members-only area. To play on this world move to a free area first."
                 20	"Invalid loginserver requested. Please try using a different world."
                 21	"You have only just left another world. Your profile will be transferred in: (number) seconds."
                 None of the above	"Unexpected server response. Please try using a different world."
                 */
                break;
        }
    }

    public void sendResponse(int one, int two, int three) {

    }
}
