package net.impl.decoder;

import net.buffers.InputBuffer;
import net.buffers.OutputBuffer;
import net.impl.NetworkConfig;
import net.impl.enc.ISAACCipher;
import net.impl.session.Client;
import util.integrity.Debug;
import util.strings.RsStringUtils;
import world.entity.player.Player;
import world.event.impl.PlayerLoginEvent;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Logger;

public final class LoginSessionDecoder implements ProtocolDecoder {
    private static final Logger logger = Logger.getLogger(LoginSessionDecoder.class.getName());
    private static final Random random = new Random(System.nanoTime());
    int count = 0;

    @Override
    public void decode(Client c) {
        InputBuffer in = c.getInputBuffer();

        if (in.remaining() < 1) {
            return;
        }

        int packetOpcode = in.readUnsignedByte();


        if (packetOpcode != LoginProtocolConstants.NEW_SESSION) {
            Debug.writeLine("Invalid packet opcode for login session " + packetOpcode);
            c.disconnect();
            return;
        }


        int loginBlockSize = in.readUnsignedByte();

        if (in.remaining() != loginBlockSize) {
            Debug.writeLine("Invalid login block size actual " + in.remaining() + "expected: " + loginBlockSize);
            c.disconnect();
            return;
        }


        int encrypedLoginBlockSize = loginBlockSize - LoginProtocolConstants.LOGIN_BLOCK_KEY;

        if (encrypedLoginBlockSize <= 0) {
            Debug.writeLine("Invalid encrypted login block size: " + encrypedLoginBlockSize);
            c.disconnect();
            return;
        }

        int magicNum = in.readUnsignedByte();

        if (LoginProtocolConstants.LOGIN_MAGIC_NUMBER != magicNum) {
            Debug.writeLine("Invalid magic number " + magicNum);
            c.disconnect();
            return;
        }


        int revision = in.readBigUnsignedWord();
        if (revision != LoginProtocolConstants.PROTOCOL_REVISION) {
            Debug.writeLine("Invalid revision " + revision);
            c.disconnect();
            return;
        }

        //Low/high mem
        in.readUnsignedByte();


        //crc
        in.skip(4 * 9);

        encrypedLoginBlockSize--;

        int size = in.readUnsignedByte();
        if (size != encrypedLoginBlockSize) {
            Debug.writeLine("Invalid encrypted login block size " + size);
            c.disconnect();
            return;
        }

        ISAACCipher decryptor;
        ISAACCipher encryptor;
        String username;
        String password;

        if (NetworkConfig.DECODE_RSA) {
            Debug.writeLine("Decoding RSA");
            byte[] encryptionBytes = new byte[encrypedLoginBlockSize];
            in.pipeTo(encryptionBytes, 0, encryptionBytes.length);
            InputBuffer rsaBuffer = new InputBuffer();
            rsaBuffer.readFrom(ByteBuffer.wrap(new BigInteger(encryptionBytes).modPow(NetworkConfig.RSA_EXPONENT, NetworkConfig.RSA_MODULUS).toByteArray()));
            int rsaOpcode = rsaBuffer.readSignedByte();
            if (rsaOpcode != 10)
                return;
            long clientHalf = rsaBuffer.readBigSignedQWORD();
            long serverHalf = rsaBuffer.readBigSignedQWORD();
            int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf};
            decryptor = new ISAACCipher(isaacSeed);
            for (int i = 0; i < isaacSeed.length; i++)
                isaacSeed[i] += 50;
            encryptor = new ISAACCipher(isaacSeed);
            rsaBuffer.readBigSignedDWORD();
            username = RsStringUtils.readRSString(rsaBuffer);
            password = RsStringUtils.readRSString(rsaBuffer);
        } else {
            Debug.writeLine("Skipping RSA");
            in.readUnsignedByte();
            long clientSeed = in.readBigSignedQWORD();
            long serverSeed = in.readBigSignedQWORD();

            int[] sessionKey = new int[4];
            sessionKey[0] = (int) (clientSeed >> 32);
            sessionKey[1] = (int) clientSeed;
            sessionKey[2] = (int) (serverSeed >> 32);
            sessionKey[3] = (int) serverSeed;

            decryptor = new ISAACCipher(sessionKey);

            for (int i = 0; i < sessionKey.length; i++) {
                sessionKey[i] += 50;
            }
            encryptor = new ISAACCipher(sessionKey);
            username = RsStringUtils.readRSString(in);
            password = RsStringUtils.readRSString(in);
        }


        Debug.writeLine("User " + username + " is logging in");

        boolean validUsername = LoginProtocolConstants.VALID_USERNAME_PREDICATE.test(username);
        boolean validPassword = LoginProtocolConstants.VALID_PASSWORD_PREDICATE.test(password);

        if ((!validUsername || !validPassword)) {
            Debug.writeLine("Invalid username or password for user" + username);
            sendResponse(c, LoginProtocolConstants.INVALID_USERNAME_OR_PASSWORD, 0);
            return;
        }

        if (encryptor == null || decryptor == null) {
            c.disconnect();
            return;
        }

        c.setInCipher(decryptor);
        c.setOutCipher(encryptor);

        Player p = new Player();
        p.setUsername(username);
        p.setClient(c);

        p.send(new PlayerLoginEvent(p, username, password, this));
    }

    public void sendResponse(Client c, int responseCode, int playerRights) {
        Debug.writeLine("Sending login response " + responseCode + " to " + c.getPlayer().getUsername());
        c.write(OutputBuffer.create(3).writeByte(responseCode).writeByte(playerRights).writeByte(0));
    }
}
