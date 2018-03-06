package util.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaKeyPairGenerator {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2056);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        System.out.println("private:");
        System.out.println("exponent:");
        System.out.println(bytesToHex(privateKey.getPrivateExponent().toByteArray()));
        System.out.println("modulus:");
        System.out.println(bytesToHex(privateKey.getModulus().toByteArray()));

        System.out.println("public:");
        System.out.println("exponent:");
        System.out.println(bytesToHex(publicKey.getPublicExponent().toByteArray()));
        System.out.println("modulus:");
        System.out.println(bytesToHex(publicKey.getModulus().toByteArray()));


    }
}
