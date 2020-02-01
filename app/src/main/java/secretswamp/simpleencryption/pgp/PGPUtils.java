package secretswamp.simpleencryption.pgp;

import java.nio.file.Files;
import java.security.*;
import javax.crypto.*;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import android.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class PGPUtils {
    private PGPUtils (){

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
            String encryptedSecretKey = encryptSecretKey(generateKeyOperationCipher(), recipientPublicKey, secretKey);

            // Final message to transmit contains the data and the encrypted secret key
            output = Base64.encodeToString(encryptedData, Base64.DEFAULT) + encryptedSecretKey;
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
            int splitIndex = encryptedMessage.indexOf('=')+2;
            String base64EncryptedData = encryptedMessage.substring(0, splitIndex);
            String encryptedSecretKey = encryptedMessage.substring(splitIndex);

            // Decrypt the key with own private key
            SecretKey secretKey = decryptSecretKey(generateKeyOperationCipher(), myPrivateKey, encryptedSecretKey);

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
            keys = keyGen.generateKeyPair();
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return keys;
    }

    private static Cipher generateKeyOperationCipher(){
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return cipher;
    }

    private static String encryptSecretKey(Cipher keyCipher,Key publicKey,SecretKey secretKey){
        String output = "";
        try{
            keyCipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] inputData = secretKey.getEncoded();
            byte[] encryptedKey = keyCipher.doFinal(inputData);
            output = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return output;
    }

    private static SecretKey decryptSecretKey(Cipher keyCipher,Key privateKey,String encryptedData){
        SecretKey output = null;
        try{
            keyCipher.init(Cipher.DECRYPT_MODE,privateKey);
            byte[] inputData = Base64.decode(encryptedData, Base64.DEFAULT);
            byte[] decreyptedData = keyCipher.doFinal(inputData);
            output = new SecretKeySpec(decreyptedData,"AES");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return output;
    }

    private static SecureRandom getRandomnessInstance(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[124];
        random.nextBytes(bytes);
        return random;
    }

    public static String encodeKey(Key k) {
        String encodedKey = Base64.encodeToString(k.getEncoded(), Base64.DEFAULT);

        return encodedKey;
    }

    public static Key decodeKey(String encodedKey, boolean isPrivate) {
        Key k = null;
        byte[] key = Base64.decode(encodedKey, Base64.DEFAULT);
        KeyFactory kf = null;
        try {
            kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(isPrivate) {
            try {
                k = kf.generatePrivate(new PKCS8EncodedKeySpec(key));
            } catch(InvalidKeySpecException e) {
                e.printStackTrace();
            }
        } else {
            try {
                k = kf.generatePublic(new X509EncodedKeySpec(key));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return k;
    }



}