package secretswamp.simpleencryption;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Context;

import java.security.KeyPair;

import secretswamp.simpleencryption.pgp.PGPUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        checkSharedPreferences(sharedPref);
    }

    private void checkSharedPreferences(SharedPreferences pref) {
        KeyPair kp = PGPUtils.generateKeyPair();
        if(!pref.contains("pub-key") || !pref.contains("priv-key")) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pub-key", kp.getPublic().toString());
            editor.putString("priv-key", kp.getPrivate().toString());
            editor.commit();
        }
    }

}
