package secretswamp.simpleencryption.CryptUtils;

import java.security.*;
import javax.crypto.*;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import android.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {
    private CryptUtils(){

    }

    private static SecretKey generateDataEncryptionKey(){
        KeyGenerator keyGen = null;
        SecretKey key = null;
        try{
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(getRandomnessInstance());
            key = keyGen.generateKey();
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }

        return key;
    }

    private static Cipher generateDataOperationCipher(){
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }

        return cipher;
    }

    public static String encryptMessage(String plainData, PublicKey recipientPublicKey){
        String output = "";
        try{
            SecretKey secretKey = generateDataEncryptionKey();

            // Encrypt the data portion of the message
            Cipher dataCipher = generateDataOperationCipher();
            dataCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] inputData = plainData.getBytes("UTF-8");
            byte[] encryptedData = dataCipher.doFinal(inputData);

            // Encrypt the key itself with the recipient's public key
            String encryptedSecretKey = encryptSecretKey(recipientPublicKey, secretKey);

            // Final message to transmit contains the data and the encrypted secret key
            output = encryptedSecretKey + "," + Base64.encodeToString(encryptedData, Base64.DEFAULT);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return output;
    }

    public static String decryptMessage(String encryptedMessage, PrivateKey myPrivateKey){
        String output = "";
        try{
            // Split the data and encrypted secret key
            int splitIndex = encryptedMessage.indexOf(",");
            String encryptedSecretKey = encryptedMessage.substring(0, splitIndex);
            String base64EncryptedData= encryptedMessage.substring(splitIndex+1);

            // Decrypt the key with own private key
            SecretKey secretKey = decryptSecretKey(myPrivateKey, encryptedSecretKey);

            // Decrypt the data portion of the message
            Cipher dataCipher = generateDataOperationCipher();
            dataCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] inputData = Base64.decode(base64EncryptedData, Base64.DEFAULT);
            byte[] decryptedData = dataCipher.doFinal(inputData);

            output = new String(decryptedData,"UTF-8");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return output;
    }

    public static KeyPair generateKeyPair(){
        KeyPairGenerator keyGen = null;
        KeyPair keys = null;
        try{
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            keys = keyGen.generateKeyPair();
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return keys;
    }

    private static String encryptSecretKey(Key publicKey,SecretKey secretKey){
        String output = "";
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] inputData = secretKey.getEncoded();
            byte[] encryptedKey = cipher.doFinal(inputData);
            output = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return output;
    }

    private static SecretKey decryptSecretKey(Key privateKey, String encryptedData){
        try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] inputData = Base64.decode(encryptedData, Base64.DEFAULT);
            byte[] decryptedData = cipher.doFinal(inputData);
            return new SecretKeySpec(decryptedData,"AES");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    private static SecureRandom getRandomnessInstance(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        return random;
    }

    public static String encodePublic(PublicKey pubKey) {
        byte[] encodedPublicKey = pubKey.getEncoded();
        return Base64.encodeToString(encodedPublicKey, Base64.DEFAULT);
    }

    public static String encodePrivate(PrivateKey privKey) {
        byte[] encodedPrivateKey = privKey.getEncoded();
        return Base64.encodeToString(encodedPrivateKey, Base64.DEFAULT);
    }

    public static PublicKey decodePublic(String key) {
        byte[] publicBytes = Base64.decode(key, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory;
        PublicKey pubKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(keySpec);
        } catch(NoSuchAlgorithmException a) {} catch(InvalidKeySpecException b) {};
        return pubKey;
    }

    public static PrivateKey decodePrivate(String key) {
        byte[] privateBytes = Base64.decode(key, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory;
        PrivateKey privKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privKey = keyFactory.generatePrivate(keySpec);
        } catch(NoSuchAlgorithmException a) {} catch(InvalidKeySpecException b) {};
        return privKey;
    }

}