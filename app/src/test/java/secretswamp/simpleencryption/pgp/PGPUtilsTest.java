package secretswamp.simpleencryption.pgp;

import org.junit.Test;
import java.security.KeyPair;

import static org.junit.Assert.*;

public class PGPUtilsTest {

    @Test
    public void main() {
        String data = "dshfasdfsdlfs";
        KeyPair aliceKeyPair = PGPUtils.generateUserKeyPair();
        KeyPair bobKeyPair = PGPUtils.generateUserKeyPair();

        // Sending a message from alice to bob:

        System.out.println("The message before alice encrypts is: \n" + data);

        String encryptedMessage = PGPUtils.encryptMessage(data, bobKeyPair.getPublic());
        System.out.println("The encrypted message in transit is: \n" + encryptedMessage);

        String decryptedMessage = PGPUtils.decryptMessage(encryptedMessage, bobKeyPair.getPrivate());
        System.out.println("The message after bob decrypts is: ");
    }
}