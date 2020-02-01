package secretswamp.simpleencryption.pgp;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.bcpg.HashAlgorithmTags;
import org.spongycastle.openpgp.PGPEncryptedData;
import org.spongycastle.openpgp.PGPKeyPair;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPSecretKey;
import org.spongycastle.openpgp.PGPSignature;
import org.spongycastle.openpgp.operator.PGPDigestCalculator;
import org.spongycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.spongycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.spongycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.spongycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.Date;

/**
 * Generates a PGP public key and secret keypair using RSA.
 * Useful if they have not been generated.
 */

public class GenerateKeyPair {

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public static void GenerateKeyPair(String identity, char[] passphrase, int keysize, boolean forceOverWrite) throws Exception {
        // Generates the keypair.
        System.out.println("Generating keypair with identity \"" + identity + "\"");

        File privateKeyFile = new File("src/main/java/secretswamp/simpleencryption/pgp/secret.asc");
        File publicKeyFile = new File("src/main/java/secretswamp/simpleencryption/pgp/pub.asc");

        // Check to see if the keys already exist (and not overwriting) before generating the keypair.
        if(privateKeyFile.exists() && publicKeyFile.exists() && !forceOverWrite) {
            System.out.println("A keypair already exists.");
            return;
        }

        FileOutputStream privateKeyStream = new FileOutputStream(privateKeyFile);
        FileOutputStream publicKeyStream = new FileOutputStream(publicKeyFile);

        //TODO: ArmourOutputStream

        // Create a keypair generator
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "SC");

        // Generate the keypair with a keysize
        gen.initialize(keysize);
        KeyPair keypair = gen.generateKeyPair();

        // Generate the PGP keypair from our Java keypair
        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
        PGPKeyPair pgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keypair, new Date());
        PGPSecretKey secretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, pgpKeyPair, identity, sha1Calc, null, null, new JcaPGPContentSignerBuilder(pgpKeyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc).setProvider("SC").build(passphrase));
        PGPPublicKey publicKey = secretKey.getPublicKey();

        // Encode the secret and public keys to the respective streams and close.
        secretKey.encode(privateKeyStream);
        publicKey.encode(publicKeyStream);
        privateKeyStream.close();
        publicKeyStream.close();
    }
}

