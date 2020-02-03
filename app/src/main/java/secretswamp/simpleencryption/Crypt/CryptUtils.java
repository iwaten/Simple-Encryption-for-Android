package secretswamp.simpleencryption.Crypt;

import java.security.*;
import javax.crypto.*;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import android.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {
    private CryptUtils(){
    }

    private static SecretKey generateDataEncryptionKey(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(getRandomnessInstance());
            return keyGen.generateKey();
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    private static Cipher generateDataOperationCipher(){
        try{
            return Cipher.getInstance("AES/ECB/PKCS5Padding");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptMessage(String plainData, PublicKey recipientPublicKey){
        try{
            // Encrypt the data portion of the message
            SecretKey secretKey = generateDataEncryptionKey();
            Cipher dataCipher = generateDataOperationCipher();
            dataCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] inputData = plainData.getBytes("UTF-8");
            byte[] encryptedData = dataCipher.doFinal(inputData);

            // Encrypt the data key itself with the recipient's public key
            String encryptedSecretKey = encryptSecretKey(recipientPublicKey, secretKey);

            // Encrypted message contains the data and the encrypted secret key
            return encryptedSecretKey + "," + Base64.encodeToString(encryptedData, Base64.NO_WRAP);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptMessage(String encryptedMessage, PrivateKey myPrivateKey){
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
            byte[] inputData = Base64.decode(base64EncryptedData, Base64.NO_WRAP);
            byte[] decryptedData = dataCipher.doFinal(inputData);

            return new String(decryptedData,"UTF-8");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair generateKeyPair(){
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            return keyGen.generateKeyPair();
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String encryptSecretKey(Key publicKey,SecretKey secretKey){
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] inputData = secretKey.getEncoded();
            byte[] encryptedKey = cipher.doFinal(inputData);
            return Base64.encodeToString(encryptedKey, Base64.NO_WRAP);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKey decryptSecretKey(Key privateKey, String encryptedData){
        try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] inputData = Base64.decode(encryptedData, Base64.NO_WRAP);
            byte[] decryptedData = cipher.doFinal(inputData);
            return new SecretKeySpec(decryptedData,"AES");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    private static SecureRandom getRandomnessInstance(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return random;
    }

    public static String encodePublicKeyToBase64(PublicKey publicKey) {
        try {
            return new String(Base64.encode(publicKey.getEncoded(), Base64.NO_WRAP), "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey decodePublicKeyFromBase64(String publicKeyB64) {
        byte[] publicKeyBytes = Base64.decode(publicKeyB64.getBytes(), Base64.NO_WRAP);
        X509EncodedKeySpec pbeks = new X509EncodedKeySpec(publicKeyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(pbeks);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}