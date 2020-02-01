import java.security.*;
import javax.crypto.*;
import java.io.*;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;

public class PGPUtil{
    private PGPUtil(){

    }

    public static SecretKey generateDataEncryptionKey(){
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

    public static Cipher generateDataOperationCipher(){
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        
        return cipher;
    }

    public static String encryptData(Cipher dataCipher,SecretKey secretKey,String plainData){
        String output = "";
        try{
            dataCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] inputData = plainData.getBytes("UTF-8");
            byte[] encryptedData = dataCipher.doFinal(inputData);
            output = Base64.getEncoder().encodeToString(encryptedData);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return output;
    }

    public static String decryptData(Cipher dataCipher,SecretKey secretKey,String encryptedData){
        String output = "";
        try{
            dataCipher.init(Cipher.DECRYPT_MODE,secretKey);
            byte[] inputData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = dataCipher.doFinal(inputData);
            output = new String(decryptedData,"UTF-8"); 
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return output;
    }

    public static KeyPair generateUserKeyPair(){
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

    public static Cipher generateKeyOperationCipher(){
        Cipher cipher = null;
        try{
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return cipher;
    }

    public static String encryptSecretKey(Cipher keyCipher,Key publicKey,SecretKey secretKey){
        String output = "";
        try{
            keyCipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] inputData = secretKey.getEncoded();
            byte[] encryptedKey = keyCipher.doFinal(inputData);
            output = Base64.getEncoder().encodeToString(encryptedKey);
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return output;
    }

    public static SecretKey decryptSecretKey(Cipher keyCipher,Key privateKey,String encryptedData){
        SecretKey output = null;
        try{
            keyCipher.init(Cipher.DECRYPT_MODE,privateKey);
            byte[] inputData = Base64.getDecoder().decode(encryptedData);
            byte[] decreyptedData = keyCipher.doFinal(inputData);
            output = new SecretKeySpec(decreyptedData,"AES");
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }
        return output;
    }

    public static SecureRandom getRandomnessInstance(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[124];
        random.nextBytes(bytes);
        return random;
    }

    public static void main(String [] args)throws UnsupportedEncodingException{
        String data = "dshfasdfsdlfs";
        SecretKey secretKey = generateDataEncryptionKey();
        Cipher dataCipher = generateDataOperationCipher();
        String encryptedBase64 = encryptData(dataCipher,secretKey,data);
        System.out.println(encryptedBase64);
        KeyPair userKeyPair = generateUserKeyPair();
        Cipher keyCipher = generateKeyOperationCipher();
        String encryptedSecretKey = encryptSecretKey(keyCipher,userKeyPair.getPublic(),secretKey);
        System.out.println("encrypted secret key: \n\n\n");
        System.out.println(encryptedSecretKey);

        SecretKey actualSecretKey = decryptSecretKey(keyCipher,userKeyPair.getPrivate(),encryptedSecretKey);
        String decryptedData = decryptData(dataCipher,actualSecretKey,encryptedBase64);

        System.out.println("\n\n\n");
        System.out.println(actualSecretKey.getEncoded());
        System.out.println("\n\n\n");
        System.out.println(decryptedData);
    }
}