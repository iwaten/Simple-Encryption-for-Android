package secretswamp.simpleencryption.Crypt;

import java.security.*;
import java.io.*;
import java.util.*;

import android.content.Context;
import android.util.Log;

//Author:Lee added for easy temp key storage without database

public class KeyStore{

    private PrivateKey myPrivateKey;
    private PublicKey myPublicKey;
    private Map<String,PublicKey> externalPublicKeys;
    private static KeyStore general = new KeyStore();

    private KeyStore(){
        myPrivateKey = null;
        myPublicKey = null;
        externalPublicKeys = new HashMap<String,PublicKey>();
    }

    public static KeyStore getKeyStoreInstance(){
        return general;
    }

    public PrivateKey getMyPrivateKey(){
        return myPrivateKey;
    }

    public PublicKey getMyPublicKey(){
        return myPublicKey;
    }

    public PublicKey getPublicKeyFrom(String name){
        return externalPublicKeys.get(name);
    }

    public void setMyPrivateKey(PrivateKey key){
        myPrivateKey = key;
    }
    public void setMyPublicKey(PublicKey key){
        myPublicKey = key;
    }

    public void addExternalPublicKey(String name,PublicKey key){
        externalPublicKeys.put(name,key);
    }

    public void loadKeys(Context context){
        ObjectInputStream input;
        String filename = "keyInfo.txt";

        try {
            File inputFile = new File(context.getFilesDir().getAbsolutePath()+File.separator+filename);
            if(!inputFile.exists()){
                Log.v("FileNotFound","Creating new file at location");
                inputFile.createNewFile();
                return;
            }
            input = new ObjectInputStream(new FileInputStream(inputFile));
            myPrivateKey = (PrivateKey) input.readObject();
            myPublicKey = (PublicKey) input.readObject();
            externalPublicKeys = (Map<String,PublicKey>) input.readObject();
            input.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void storeKeys(Context context) {
        String filename = "keyInfo.txt";
        ObjectOutput out = null;

        try {
            File outFile = new File(context.getFilesDir().getAbsolutePath()+File.separator+filename);
            if(!outFile.exists()){
                Log.v("FileNotFound","Creating new file at location");
                outFile.createNewFile();
            }
            out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(myPrivateKey);
            out.writeObject(myPublicKey);
            out.writeObject(externalPublicKeys);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}